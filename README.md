# 📚 TextHip (텍스트힙)

<p align="center">
  <strong>"나만의 구절을 수집하고 공유하는 2030세대를 위한 소셜 독서 및 문화 창작 플랫폼"</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/version-0.0.1-blue.svg" alt="version" />
  <img src="https://img.shields.io/badge/platform-iOS%20%7C%20Android-brightgreen.svg" alt="platform" />
  <img src="https://img.shields.io/badge/license-MIT-lightgrey.svg" alt="license" />
</p>

---

## ✨ 프로젝트 소개

"독서는 고리타분하고 정적이다"라는 편견 속에서, 저희는 새로운 가능성을 발견했습니다. 책을 사랑하고 SNS로 자신을 표현하는 데 익숙한 2030세대는 책을 읽고 난 후의 영감과 감성을 인스타그램처럼 멋지게 기록하고, 비슷한 취향의 사람들과 감각적으로 교류할 공간을 원하고 있습니다.

**TextHip**은 바로 이 지점에서 출발합니다. 기존의 '디지털 도서관' 형태의 서비스나 기능적인 커뮤니티를 넘어, 독서 경험을 하나의 **작품**으로 만들고 개인의 **정체성**으로 표현하고자 하는 2030세대의 숨은 욕구를 해결하는 서비스입니다.

단순한 독서 기록을 넘어, **'읽는 행위'를 '창작 활동'으로** 확장하며 새로운 독서 문화를 만들어갑니다.

## 프로젝트 구조

프로젝트는 크게 4개의 핵심 도메인(auth, user, book, booklist)과 3개의 지원 계층(config, common, exception)으로 구성되어 있습니다.

com.texthip.texthip_server

auth          # 🔐 인증/인가 (JWT, OAuth2)

user          # 🧑‍💻 사용자 및 프로필

book          # 📚 도서 정보 (외부 API 연동)

booklist      # 📑 사용자의 도서 목록

config        # ⚙️ 애플리케이션 핵심 설정 (Security, CORS 등)

common        # 📦 공통 응답 DTO 등

exception     # 🛡️ 전역 예외 처리

