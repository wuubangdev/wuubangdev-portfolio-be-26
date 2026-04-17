# Frontend Integration Guide

Tài liệu này dành riêng cho frontend để nối API nhanh và đúng contract backend hiện tại.

## Base URL

```ts
export const API_BASE_URL = "http://localhost:8080";
```

## 1. Headers chuẩn

### Public request

```ts
const headers = {
  "Content-Type": "application/json",
};
```

### Auth request

```ts
const headers = {
  "Content-Type": "application/json",
  Authorization: `Bearer ${accessToken}`,
};
```

### Request có locale

```ts
const lang = "vi"; // hoặc "en"
fetch(`${API_BASE_URL}/api/v1/profile?lang=${lang}`);
```

## 2. TypeScript Types

### Shared

```ts
export type LocaleCode = "vi" | "en";

export interface PageResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
}

export interface TranslatedResponse {
  locale: string;
  translated: boolean;
}
```

### Auth

```ts
export type UserType = "BASIC" | "GOOGLE" | "GITHUB";

export interface LoginResponse {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
  expiresIn: number;
  refreshExpiresIn: number;
  message: string;
}

export interface UserResponse {
  username: string;
  email: string;
  roles: string[];
  enabled: boolean;
  userType: UserType;
}
```

### Profile

```ts
export interface SocialLinkDto {
  platform: string;
  url: string;
  icon: string;
}

export interface ProfileResponse extends TranslatedResponse {
  id: number | null;
  fullName: string;
  title: string;
  bio: string;
  avatarUrl: string;
  resumeUrl: string;
  location: string;
  email: string;
  phone: string;
  socialLinks: SocialLinkDto[];
}
```

### Post

```ts
export interface PostResponse extends TranslatedResponse {
  id: number;
  title: string;
  slug: string;
  category: string;
  content: string;
  summary: string;
  coverImageUrl: string;
  tags: string[];
  published: boolean;
  author: string;
  titleSeo: string;
  descriptionSeo: string;
  thumbnailSeo: string;
  seoKeywords: string[];
  canonicalUrl: string;
  indexable: boolean;
  likes: number;
  hearts: number;
  commentsCount: number;
  shares: number;
  status: string;
  createdAt: string;
  displayOrder: number;
  isHidden: boolean;
}

export interface PostEngagementResponse {
  postId: number;
  slug: string;
  likes: number;
  shares: number;
}
```

### Project

```ts
export interface ProjectResponse extends TranslatedResponse {
  id: number;
  title: string;
  slug: string;
  category: string;
  tags: string[];
  description: string;
  content: string;
  techStack: string[];
  imageUrl: string;
  projectUrl: string;
  githubUrl: string;
  groupName: string;
  featured: boolean;
  displayOrder: number;
  titleSeo: string;
  descriptionSeo: string;
  thumbnailSeo: string;
  seoKeywords: string[];
  canonicalUrl: string;
  indexable: boolean;
  createdAt: string;
}
```

### Experience

```ts
export interface ExperienceResponse extends TranslatedResponse {
  id: number;
  company: string;
  companyUrl: string;
  role: string;
  description: string;
  logoUrl: string;
  startDate: string | null;
  endDate: string | null;
  location: string;
  displayOrder: number;
  isHidden: boolean;
}
```

### Education

```ts
export interface EducationResponse extends TranslatedResponse {
  id: number;
  institution: string;
  degree: string;
  fieldOfStudy: string;
  startDate: string | null;
  endDate: string | null;
  description: string;
  logoUrl: string;
  location: string;
  displayOrder: number;
  isPublic: boolean;
}
```

### Skill

```ts
export interface SkillResponse {
  id: number;
  name: string;
  category: string;
  level: number;
  icon: string;
  displayOrder: number;
  isHidden: boolean;
}
```

### Contact

```ts
export interface ContactResponse {
  id: number;
  name: string;
  email: string;
  subject: string;
  message: string;
  read: boolean;
  status: string;
  createdAt: string;
}
```

## 3. Auth Flow

## Login

```ts
async function login(username: string, password: string) {
  const res = await fetch(`${API_BASE_URL}/api/v1/auth/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ username, password }),
  });

  const data: LoginResponse = await res.json();

  localStorage.setItem("accessToken", data.accessToken);
  localStorage.setItem("refreshToken", data.refreshToken);

  return data;
}
```

## Refresh token

```ts
async function refreshAccessToken() {
  const refreshToken = localStorage.getItem("refreshToken");

  const res = await fetch(`${API_BASE_URL}/api/v1/auth/refresh`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ refreshToken }),
  });

  if (!res.ok) {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
    throw new Error("Refresh token failed");
  }

  const data: LoginResponse = await res.json();
  localStorage.setItem("accessToken", data.accessToken);
  localStorage.setItem("refreshToken", data.refreshToken);
  return data.accessToken;
}
```

## Fetch wrapper gợi ý

```ts
async function apiFetch<T>(input: string, init?: RequestInit): Promise<T> {
  const accessToken = localStorage.getItem("accessToken");

  let res = await fetch(input, {
    ...init,
    headers: {
      "Content-Type": "application/json",
      ...(accessToken ? { Authorization: `Bearer ${accessToken}` } : {}),
      ...(init?.headers || {}),
    },
  });

  if (res.status === 401 && localStorage.getItem("refreshToken")) {
    const newAccessToken = await refreshAccessToken();
    res = await fetch(input, {
      ...init,
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${newAccessToken}`,
        ...(init?.headers || {}),
      },
    });
  }

  if (!res.ok) {
    throw new Error(`Request failed: ${res.status}`);
  }

  const contentType = res.headers.get("content-type") || "";
  if (contentType.includes("application/json")) {
    return res.json() as Promise<T>;
  }

  return res.text() as T;
}
```

## 4. Public Page Data Fetch

## Home / portfolio page

Gợi ý gọi:

```ts
const [profile, social, setting, skills, experiences, educations] =
  await Promise.all([
    apiFetch<ProfileResponse>(`${API_BASE_URL}/api/v1/profile?lang=${lang}`),
    apiFetch(`${API_BASE_URL}/api/v1/social`),
    apiFetch(`${API_BASE_URL}/api/v1/setting`),
    apiFetch<SkillResponse[]>(`${API_BASE_URL}/api/v1/skills`),
    apiFetch<ExperienceResponse[]>(`${API_BASE_URL}/api/v1/experiences?lang=${lang}`),
    apiFetch<EducationResponse[]>(`${API_BASE_URL}/api/v1/educations?lang=${lang}`),
  ]);
```

## Blog list

```ts
const posts = await apiFetch<PageResponse<PostResponse>>(
  `${API_BASE_URL}/api/v1/posts/paged?page=0&size=10&lang=${lang}`
);
```

## Blog detail

```ts
const post = await apiFetch<PostResponse>(
  `${API_BASE_URL}/api/v1/posts/${slug}?lang=${lang}`
);

const related = await apiFetch<PostResponse[]>(
  `${API_BASE_URL}/api/v1/posts/${post.id}/related?limit=5&lang=${lang}`
);
```

## Project list

```ts
const projects = await apiFetch<PageResponse<ProjectResponse>>(
  `${API_BASE_URL}/api/v1/projects/paged?page=0&size=9&featured=true&lang=${lang}`
);
```

## Project detail

```ts
const project = await apiFetch<ProjectResponse>(
  `${API_BASE_URL}/api/v1/projects/${slug}?lang=${lang}`
);

const related = await apiFetch<ProjectResponse[]>(
  `${API_BASE_URL}/api/v1/projects/${slug}/related?limit=3&lang=${lang}`
);
```

## 5. SEO mapping cho FE

Với `post` và `project`, FE có thể map thẳng:

```ts
function mapSeoMeta(input: {
  titleSeo: string;
  descriptionSeo: string;
  thumbnailSeo: string;
  canonicalUrl: string;
  indexable: boolean;
}) {
  return {
    title: input.titleSeo,
    description: input.descriptionSeo,
    image: input.thumbnailSeo,
    canonical: input.canonicalUrl,
    robots: input.indexable ? "index,follow" : "noindex,nofollow",
  };
}
```

## 6. Multilingual UI rule

Với các entity có translation:

```ts
if (!data.translated) {
  console.log("Dang fallback ve noi dung goc");
}
```

Gợi ý UX:

- public page: không cần hiện gì nếu `translated=false`
- admin CMS preview: nên hiện badge `Fallback`

## 7. CMS Write Flow

Có 2 cách lưu:

## Cách 1. Gửi cùng payload chính

Ví dụ `post`:

```json
{
  "title": "Bai viet goc",
  "slug": "bai-viet-goc",
  "category": "Technology",
  "content": "Noi dung goc",
  "summary": "Tom tat",
  "coverImageUrl": "https://...",
  "tags": ["spring", "java"],
  "published": true,
  "author": "Wuu Bang Dev",
  "titleSeo": "SEO title",
  "descriptionSeo": "SEO description",
  "thumbnailSeo": "https://...",
  "seoKeywords": ["spring", "java"],
  "canonicalUrl": "https://...",
  "indexable": true,
  "displayOrder": 1,
  "isHidden": false,
  "translations": [
    {
      "locale": "en",
      "title": "English title",
      "content": "English content",
      "summary": "English summary",
      "titleSeo": "English SEO title",
      "descriptionSeo": "English SEO description",
      "seoKeywords": ["spring", "java"]
    }
  ]
}
```

## Cách 2. Lưu dữ liệu gốc trước, rồi lưu translation riêng

Ví dụ `post` translation:

```ts
await apiFetch<PostResponse>(
  `${API_BASE_URL}/api/v1/admin/posts/${postId}/translations/en`,
  {
    method: "PUT",
    body: JSON.stringify({
      title: "English title",
      content: "English content",
      summary: "English summary",
      titleSeo: "English SEO title",
      descriptionSeo: "English SEO description",
      seoKeywords: ["spring", "java"],
    }),
  }
);
```

## 8. Admin Translation Endpoints

### Post

```text
PUT /api/v1/admin/posts/{id}/translations/{locale}
```

Body:

```json
{
  "title": "English title",
  "content": "English content",
  "summary": "English summary",
  "titleSeo": "English SEO title",
  "descriptionSeo": "English SEO description",
  "seoKeywords": ["spring", "java"]
}
```

### Project

```text
PUT /api/v1/admin/projects/{id}/translations/{locale}
```

Body:

```json
{
  "title": "English title",
  "description": "English description",
  "content": "English content",
  "titleSeo": "English SEO title",
  "descriptionSeo": "English SEO description",
  "seoKeywords": ["spring", "mongodb"]
}
```

### Experience

```text
PUT /api/v1/admin/experiences/{id}/translations/{locale}
```

Body:

```json
{
  "company": "Company A",
  "role": "Backend Developer",
  "description": "English description",
  "location": "Da Nang"
}
```

### Education

```text
PUT /api/v1/educations/{id}/translations/{locale}
```

Body:

```json
{
  "institution": "FPT University",
  "degree": "Bachelor",
  "fieldOfStudy": "Software Engineering",
  "description": "English description",
  "location": "Da Nang"
}
```

### Profile

```text
PUT /api/v1/admin/profile/translations/{locale}
```

Body:

```json
{
  "fullName": "Wuu Bang Dev",
  "title": "Full Stack Developer",
  "bio": "English bio",
  "location": "Da Nang"
}
```

## 9. Contact Form

```ts
await apiFetch<ContactResponse>(`${API_BASE_URL}/api/v1/contact`, {
  method: "POST",
  body: JSON.stringify({
    name: "John Doe",
    email: "john@example.com",
    subject: "Need collaboration",
    message: "Hello, I want to contact you.",
  }),
});
```

## 10. Post Like / Share

```ts
await apiFetch<PostEngagementResponse>(
  `${API_BASE_URL}/api/v1/posts/${slug}/like`,
  { method: "POST" }
);

await apiFetch<PostEngagementResponse>(
  `${API_BASE_URL}/api/v1/posts/${slug}/like`,
  { method: "DELETE" }
);

await apiFetch<PostEngagementResponse>(
  `${API_BASE_URL}/api/v1/posts/${slug}/share`,
  { method: "POST" }
);
```

## 11. FE Pitfalls cần tránh

- Đừng giả định mọi endpoint trả `{ data: ... }`
- Đừng giả định mọi endpoint trả JSON object
  - auth message endpoints trả plain string
- Với multilingual entity:
  - luôn gửi `?lang=...`
  - luôn đọc `translated`
- Route admin của `education` hiện chưa theo prefix `/api/v1/admin/...`
- Nếu FE dev chạy `localhost:5173`, backend hiện chưa mở CORS sẵn

## 12. Recommended FE Folder Shape

```ts
src/
  lib/
    api.ts
    auth.ts
  types/
    api.ts
  features/
    auth/
    blog/
    projects/
    cms/
```

## 13. Quick Checklist

- login lưu cả `accessToken` và `refreshToken`
- request admin luôn kèm `Authorization`
- page public có content multilingual luôn thêm `?lang=...`
- đọc `translated` để biết fallback
- list phân trang dùng `PageResponse<T>`
- CMS dùng endpoint translation riêng khi sửa từng locale
