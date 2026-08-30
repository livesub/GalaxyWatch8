# 갤럭시 워치8 클래식 워치페이스 제작 규칙 (CLAUDE.md)

⚠️ 문제 생기면 이 커밋/태그로 원복(최신 안정 기준점): 커밋 f197281 / 태그 dev-final-stable-v6 (git reset --hard dev-final-stable-v6) — 달의 위상 아이콘 구현 + 콤플리케이션/UI 다수 수정

## 1. 프로젝트 개요
- 목적: 갤럭시 워치8 클래식 개인용 워치페이스, VSCode + Claude Code CLI 바이브코딩으로 제작
- 포맷: Watch Face Format (WFF) v2, watchface.xml 선언형 (v5 상향 시도했으나 실기기/에뮬레이터에서 패키지 자체 거부(FavoriteOperationException) 확인 → v2로 최종 확정. isAutoSize/verticalAlign/minSize(오토사이즈 부속 속성)는 v2 XSD상 미지원 리포트 뜨지만 실제 렌더 정상 동작 확인되어 그대로 유지)

## 2. 대상 기기 사양
- 디스플레이: 1.34" Super AMOLED, 원형
- 해상도: 438 x 438 px, 약 327ppi
- 최대 밝기: 3000nit (peak)

## 3. 제작시 준수 사항

### 3-1. 포맷/구조
- 루트 엘리먼트는 반드시 `<WatchFace>`로 시작
- 목표 버전 WFF v2 확정(AndroidManifest format.version="2") — v5는 설치 자체가 거부되어 폐기, 최소 Wear OS 5 / API 34 요구
- 2026년 1월부터 모든 Wear OS 기기에 WFF 형식 설치 필수(레거시 형식 불가)

### 3-2. 메모리 제한
- 일반(인터랙티브) 모드: 100MB 이하
- AOD(상시 사용) 모드: 10MB 이하

### 3-3. AOD(상시 사용) 규칙
- 사용 픽셀 비율 15% 이하 권장 (초과 시 반려 가능)
- 어두운 색상 위주 구성 (배터리 절감)
- 갱신이 잦은 요소(초침 등) 최소화

### 3-4. 이미지/애니메이션
- 불필요한 고해상도 이미지 금지, 실제 표시 크기에 맞게 리사이즈
- 투명 여백 픽셀 제거
- GIF 대신 WebP 사용 (용량↓, 렌더링 부담↓, 전력↓)
- 유사 목적 이미지는 하나로 통합
- 날씨 아이콘 원본은 480x480 PNG → 실사용 전 반드시 WebP 변환 + 실제 표시 크기로 리사이즈
- 배경 이미지는 사각형으로 생성되더라도 반드시 원형 크롭 후 사용 (제미나이 등으로 생성시 공통 규칙)

### 3-5. 검증
- 배포 전 WFF Validator(google/watchface)로 오류·메모리 사용량 검증 필수

## 4. 리소스 경로 (고정 — 매번 재지시 불필요, 실제 스캐폴드 기준 정정됨)
- 실제 프로젝트 구조: root + `:watchface` 서브모듈. 빌드/리소스는 전부 서브모듈 기준
- watchface.xml: `watchface/src/main/res/raw/watchface.xml`
- watch_face_info.xml: `watchface/src/main/res/xml/watch_face_info.xml` (Editable/Preview 설정)
- 프로덕션 이미지(drawable): `watchface/src/main/res/drawable` (서브모듈 내부에 있어야 watchface.xml에서 참조 가능 — root의 `src/main/res/drawable`가 아님)
- 원본 참조 이미지(빌드 제외, 참고용): `src/main/design/orgi.png` (root, 그대로 유지)
- 날씨 아이콘 32종 + WFF CONDITION 매핑표: `docs/weather_icons.md` (root)
- 단계별 개발 지시서: `docs/개발_지시서.md` (root)
- 진행상황 기록: `PROGRESS.md` (root)
- `assets/`(root, 빈 폴더): 템플릿 생성 잔여물로 추정, 용도 없으면 정리 대상

### 4-1. 처음부터 빌드 시 필요한 리소스 파일 전체 목록 (사전 준비 필수)
1. **날씨 아이콘 32종**(day 16 + night 16, 각 480x480 PNG RGBA 원본) — 파일명/매핑표는 `docs/weather_icons.md` 참조. 빌드 스크립트가 WebP 변환+실사용 크기 리사이즈 자동 처리(2단계)
2. **강수확률 우산 아이콘 1종**(`rain_umbrella`, 원본 260x236, 투명배경 PNG) — 32종과 동일 변환 파이프라인으로 WebP화
2-1. **하단 알약 아이콘 3종**(`icon_battery.png`, `icon_heart.png`, `icon_steps.png`) — watchface.xml에서 실제 참조되는 별도 이미지 파일(코드로 직접 그리는 벡터가 아님), 워치배터리/심박수/걸음수 알약 좌측에 배치
3. **배경 이미지 2종**(day/night, 각 1장) — 6-1-1의 그라디언트 톤 기준으로 생성(제미나이 등), 사각형으로 나와도 반드시 원형 크롭 후 사용
4. **폰트 파일 2종**: `notosanskr_bold.ttf`, `notosanskr_regular.ttf`(SIL OFL) — 처음부터 서브셋 버전으로 준비 권장(21-17-5 확정 76자 세트: 숫자 0-9, 콜론, 마침표, 가운뎃점(·), 도(°), 퍼센트(%), "오전""오후", 요일 한글 7개, "강수확률""워치배터리""걸음수""심박수""정보 없음""날씨 데이터 로드 실패, 실기기에서 확인 필요", `docs/weather_icons.md` 날씨 코멘트 문구 전부, "바람개비 워치8 클래식") — 처음부터 이 문자셋으로 서브셋하면 21-17-5 재작업 불필요
5. **미리보기 이미지**(`preview.png`) — 사전 준비 불필요. 전체 빌드 완성 후 에뮬레이터 실제 화면을 캡처해서 만듦(22단계)
6. `src/main/design/orgi.png`(1024x1024 원본 참조 목업) — 선택사항, 빌드 결과물에는 미포함, 좌표 비율 참고용으로만 필요(5-2 정식 좌표표가 있으므로 사실상 불필요)

### 4-2. 스캐폴드 잔재 (빌드에 불필요, 정리 대상)
- `watchface/src/main/res/drawable/hour.xml`, `minute.xml`, `second.xml` — 아날로그 시계바늘용 VectorDrawable 3종, 현재 디지털 시계(DigitalClock/TimeText) 방식이라 어디서도 참조 안 됨. 새로 만들 때 생성할 필요 없음
- `assets/`(root, 빈 폴더) — 위 4번 참조, 정리 대상

### 4-3. watchface.xml 기타 확정 설정 (재현 시 누락 주의)
- `<Metadata key="CLOCK_TYPE" value="DIGITAL" />` — 아날로그 시계바늘 미사용 명시
- `<Metadata key="PREVIEW_TIME" value="17:10:37" />` — 워치페이스 목록 썸네일에 쓰이는 고정 미리보기 시각(하드코딩 확정값)
- Scene 최상위 `backgroundColor="#000000"`(배경 이미지 아래 깔리는 기본 검정)

## 5. 화면 레이아웃 스펙 (원형 캔버스 기준, 위→아래, 데모 이미지 다수 라운드 검수로 확정)
- 전체 원칙: 빈 공간 최소화하며 꽉 차게 배치하되, 개별 요소를 과도하게 키우지 않을 것(가독성과 균형 우선)
1. 알림 점: 읽지 않은 알림 있을 때만 표시(없으면 숨김), 좌측 빈공간(중단 좌측 콤플리케이션 바깥, 원 세로 중앙 높이)으로 위치 변경 확정(기존 최상단 중앙에서 이동) — 점 바로 밑에 읽지 않은 알림 개수(숫자)도 같은 표시조건으로 병기
1-1. 최고/최저 기온: 알림 점이 빠지면서 비게 된 최상단 중앙에 배치, 위치기반 오늘 최고/최저 기온 표시(하드코딩 금지) — 필드 `WEATHER.DAYS.0.TEMPERATURE_LOW`/`WEATHER.DAYS.0.TEMPERATURE_HIGH` 확정(DAY_TEMPERATURE_LOW/HIGH 형태 아님), 화씨→섭씨 변환 분기 포함
1-2. 강수확률: 우측 빈공간(중단 우측 콤플리케이션 바깥, 원 세로 중앙 높이)에 우산 아이콘+라벨+퍼센트 3줄로 배치, 위치기반 날씨 데이터 바인딩(하드코딩 금지)
2. 좌상단: 일출/일몰 통합 슬롯(SLOT_SUNRISE 1개만 사용, 우상단 슬롯은 삭제해 빈 공간) — SUNRISE_SUNSET 시스템 데이터소스가 "다음 해 이벤트 하나"만 제공하는 플랫폼 한계로 좌/우 분리 표시 불가 확인(2026-08-30, 9-1 참조) → 라벨을 `WEATHER.IS_DAY` 조건부로 전환: 낮(true)이면 "일몰", 밤(false)이면 "일출" 표시. 값 자체(COMPLICATION.TEXT)는 그대로
3. 상단 중앙: 현재 온도 — 위치기반 현지 온도, 섭씨(℃) 고정 원함(2자리 이내). **일출(라벨)/온도/일몰(라벨) 3요소는 1행, 각 시각값/날씨코멘트는 2행**으로 정확히 같은 높이로 정렬(3열×2행 격자 구조, 온도만 유독 크거나 어긋나지 않게). 온도 바로 아래(2행)에 날씨 조건 한글 코멘트 1줄 추가 — **WEATHER.CONDITION 값에 따라 자동 전환(하드코딩 금지)**, 매핑표는 `docs/weather_icons.md`의 "날씨 코멘트 매핑" 표 참조(예시로 든 "대체로 흐림"은 PARTLY_CLOUDY 하나의 케이스일 뿐, 16개 코드 전부 매핑할 것). **한글 고정(공백 포함 최대 6자, 매핑표 문구 그대로 사용)**, 레이아웃 폭 초과 방지용 제약이라 새 문구 임의 추가 금지
4. 우상단: (삭제됨, 빈 공간) — 원래 일몰 전용 슬롯(SLOT_SUNSET)이었으나 위 2번 사유로 제거. 이 자리 활용은 보류 중(달의 위상 표시 후보, 9 참조)
5. 중앙: 날씨 캐릭터 아이콘(day/night 32종) — `docs/weather_icons.md` 매핑 기준 자동 전환, 비율 유지한 채 확대(강제 늘리기 금지), 온도 세트 행과 아이콘 사이 간격 확보(붙어 보이지 않게)
6. 상단 좌측 원형 콤플리케이션: 앱 바로가기 슬롯, 용도는 차량 앱(라벨일 뿐 사전 지정 불가, 최초엔 빈 상태 — 7-1 참조), 사용자가 편집화면에서 직접 지정
7. 상단 우측 원형 콤플리케이션: 앱 바로가기 슬롯, 용도는 AI 어시스턴트(위와 동일하게 사전 지정 불가), 사용자가 직접 지정
8. 중단 좌측 원형 콤플리케이션: 데이터 슬롯, 용도는 휴대폰 배터리(기본 EMPTY, 서드파티 앱 설치 후 사용자가 직접 지정) — 값 있을 때 아이콘 없이 숫자만(단위 없이) 원형 중앙정렬 표시
9. 중단 우측 원형 콤플리케이션: 앱 바로가기 슬롯, 용도는 음성녹음(위와 동일하게 사전 지정 불가), 사용자가 직접 지정
   - 6~9번 원형 4개는 모두 동일 크기로 통일(8번 숫자 표시가 들어가도 여유 있는 크기 기준), 단 날씨 요소보다 시각적 비중은 낮게(작게) — 4개 모두 부가 정보 취급, 날씨가 화면의 주인공
   - **미설정(EMPTY) 상태 표시 확정(재정정)**: 4개 슬롯 전부(휴대폰배터리 포함) 미설정 시에도 원형 테두리 + "+" 아이콘을 watchface.xml 자체에서 그려서 평소 화면에도 항상 보이게 처리(이전엔 폰배터리만 "원형 자체 비표시"였으나 4개 통일로 변경 확정). 콤플리케이션 편집(꾸미기) 화면에서 시스템이 자동으로 그려주는 "+"는 이것과 별개(그건 그대로 유지, 구현 불필요) — 지금 추가하는 건 편집화면이 아닌 **평소 시계 화면**에 뜨는 자체 제작 원형+"+" 플레이스홀더. 실제 값/앱이 설정되면 콤플리케이션 콘텐츠가 그 위에 렌더링되어 "+"를 덮음
10. 중앙: 날짜(위치기반 현지 날짜, 골드톤 `#FFDDAA`) + 요일(월~일 고정 7색, 3-후단 색상표 참조) — 날짜/요일 폰트 크기 동일, 가운데 정렬 고정(자릿수 바뀌어도 흔들리지 않게)
11. 메인 시계: 위치기반 현지 시각, 12시간제 오전/오후 표기 (예: 23시 → 오후 11시), 색상은 흰색+다크아웃라인(6-1 공통 규칙, 검정 아님 — 배경 어디서나 가독성 우선), 레이아웃상 가장 크게(단, 날씨/날짜 요소를 과도하게 압박하지 않는 선)
12. 메인 시계 옆: 초(seconds) 보조 표시, 크게 강조(오전/오후와 동일 코럴톤 계열)
13. 하단 알약1: 워치 배터리(%) — **독립된 타원형 3개**(하나로 합친 바 아님, 최종 원복 확정), 타원 내부에 타이틀(예: "워치 배터리")+아이콘+값 2줄 구성. 타원 배경은 반투명이되 충분히 진하게(주/야간 배경 모두에서 타원 경계가 보여야 함, 얇은 밝은 테두리 1px 병행), 텍스트는 밝은 민트 계열(흰색·요일색·온도색과 겹치지 않는 미사용 색상)
    - **아이콘/값 배치 규칙**: 아이콘은 타원 내 좌측 고정, 값(숫자)은 우측 정렬 — 자릿수가 늘어나도 아이콘 위치는 흔들리지 않고 값만 오른쪽 기준으로 왼쪽으로 확장
    - **박스 크기 통일 확정(재정정)**: 3개 알약 전부 동일 96×44px(기존 차등폭 폐기) — 걸음수(999999, 6자리) 기준으로 폭 산출, 원형 화면 안전범위(x=65~373, 실측 검증됨) 안에 3개+간격10px로 배치
    - **정렬 방식 차등 확정**: 배터리/심박수는 값(숫자) **가운데 정렬**(아이콘 오른쪽 남는 공간 기준 중앙), 걸음수만 자릿수 변동폭이 커서 값 **오른쪽 정렬**(알약 우측 테두리에서 14px 여백 고정, 기존 6px에서 확대 — 24단계) — 자릿수 바뀌어도 우측 기준점 고정
    - 아이콘과 값(텍스트)은 세로 중심 높이 동일하게 정렬(아이콘 세로 중앙 = 값 텍스트 세로 중앙)
14. 하단 알약2: 삼성 헬스 걸음 수 — 알약1과 동일 스타일(독립 타원, 타이틀 내장), 폭은 위 96px 통일 규칙 적용, 값은 오른쪽 정렬
15. 하단 알약3: 심박수(HEART_RATE) 단독 확정(스트레스 미구현) — 알약1/2와 동일 스타일, 폭은 위 96px 통일 규칙 적용, 값은 가운데 정렬
16. 최하단: 워치페이스 이름 "바람개비 워치8 클래식" — 볼드 아님(NORMAL 굵기), 별도 배경 박스 없음, 색상 피치/탠 `#F2C9A0`+다크 아웃라인(20단계 반영 완료)
   - **기술검증 완료(20단계)**: WFF 공식 `TextCircular` 엘리먼트가 곡선 텍스트를 지원하나 "Introduced in Wear OS 4" 전용 확인됨 — 본 프로젝트는 v2 확정(15단계에서 v3~v5 전부 설치 거부 확인)이라 사용 불가. 대안 ②(직선 텍스트 유지) 채택 확정, 전체 회전(대안①)은 대칭 텍스트가 한쪽으로 기울어 보여 제외
17. 배경: 주간/야간 2종만(확정) — 날씨조건 표현은 중앙 캐릭터 아이콘이 담당, 배경은 day/night 톤만 전환. 원형 크롭 필수

### 5-2. 정확한 좌표 스펙표 (438x438 캔버스 실측값, demo.html 기준 — orgi.png와 달리 비율환산 불필요, 그대로 x/y/width/height로 사용)
| 요소 | x | y | width | height | 비고 |
|---|---|---|---|---|---|
| 알림 점 | 30 | 214 | 10 | 10 | 좌측 이동 확정(기존 214,8에서 변경), 색상 `#FFA726` |
| 알림 개수 | 10 | 226 | 50 | 14 | 점과 동일 표시조건, 가운데 정렬 |
| 최고/최저 기온 | 0 | 28 | 438 | 17 | 알림점 이동으로 비워진 상단중앙, 가운데 정렬, 민트 `#C9F7DC`, 필드 WEATHER.DAYS.0.TEMPERATURE_LOW/HIGH |
| 강수확률 라벨 | 374 | 186 | 60 | 14 | "강수확률" 고정 텍스트, 가운데 정렬 (겹침버그 수정: 194→186) |
| 강수확률 아이콘 | 387 | 203 | 34 | 31 | 우산 이미지(`rain_umbrella`) (겹침버그 수정: 198→203) |
| 강수확률 값 | 374 | 237 | 60 | 16 | "NN%" 형식, 가운데 정렬, 색상 `#4FC3F7`+다크아웃라인, 필드 WEATHER.DAYS.0.CHANCE_OF_PRECIPITATION (겹침버그 수정: 224→237) |
| 일출/일몰 라벨 | 71 | 58 | 82 | 26 | 텍스트 조건부("일출"/"일몰", WEATHER.IS_DAY로 전환) |
| 온도 값 | 167 | 55 | 104 | 31 | "28°C" |
| (우측, 빈 공간) | 285 | 58 | 82 | 26 | 삭제됨(원 일몰 라벨 자리) — 미사용 |
| 일출/일몰 시각 | 71 | 89 | 82 | 26 | SLOT_SUNRISE 값(COMPLICATION.TEXT, 통합) |
| 날씨 코멘트 | 167 | 93 | 104 | 18 | 최대 6자 |
| (우측, 빈 공간) | 285 | 89 | 82 | 26 | 삭제됨(원 일몰 값 자리) — 미사용 |
| 날씨 아이콘 | 0 | 121 | 438 | 105 | 가로 전체폭 중앙정렬, 실제 아이콘은 정사각비율 유지 |
| 콤플리케이션(차량) | 70 | 130 | 52 | 52 | SLOT_TOP_LEFT |
| 콤플리케이션(AI) | 316 | 130 | 52 | 52 | SLOT_TOP_RIGHT |
| 콤플리케이션(폰배터리) | 70 | 200 | 52 | 52 | SLOT_MID_LEFT |
| 콤플리케이션(음성) | 316 | 200 | 52 | 52 | SLOT_MID_RIGHT |
| 날짜행-날짜 | 120 | 227 | 160 | 32 | align=END (23단계 실측 반영, 기존 통합표기 0/224/438/32는 폐기) |
| 날짜행-요일 | 285 | 227 | 40 | 32 | align=START, 7개 동일 x (23단계 실측 반영) |
| 오전/오후 | 78 | 264 | 32 | 59 | 세로 배치 |
| 메인 시계(시:분) | 116 | 254 | 190 | 78 | 배경 박스 없음(텍스트만) |
| 초 | 311 | 270 | 70 | 70 | 21단계에서 확장된 후 표 미갱신 상태였음 — 23단계 실측 반영(기존 49/50은 폐기) |
| 알약(워치배터리) | 65 | 340 | 96 | 44 | 값 가운데 정렬 |
| 알약(걸음수) | 171 | 340 | 96 | 44 | 값 오른쪽 정렬(우측 여백 14px), 999999 기준 |
| 알약(심박수) | 277 | 340 | 96 | 44 | 값 가운데 정렬 |
| 워치이름 텍스트 | 139 | 395 | 160 | 32 | 20단계 결정으로 직선 텍스트(곡선 아님), 가로 중앙정렬(center x=219) |

### 5-3. 폰트 크기/family 전체 목록 (watchface.xml 실측값, 재현 시 필수)
| 요소 | size | family/weight |
|---|---|---|
| 메인시계(시:분) | 74 | notosanskr_bold/BOLD |
| 오전/오후 | 27 | notosanskr_bold/BOLD |
| 초(seconds) | 51 | notosanskr_bold/BOLD |
| 날짜 | 25 | notosanskr_bold/BOLD |
| 요일(7개 공통) | 25 | notosanskr_bold/BOLD |
| 온도 | 32 | notosanskr_bold/BOLD |
| 최고/최저 기온 | 15 | notosanskr_bold/BOLD |
| 날씨 코멘트 | 17 | notosanskr_bold/BOLD |
| 일출/일몰 라벨 | 16 | notosanskr_bold/BOLD |
| 일출/일몰 시각 | 21 | notosanskr_bold/BOLD |
| 강수확률 라벨 | 11 | notosanskr_bold/BOLD |
| 강수확률 값 | 13 | notosanskr_bold/BOLD |
| 알약 라벨(3개 공통) | 12 | notosanskr_bold/BOLD |
| 알약 값 - 배터리 | 19 | notosanskr_bold/BOLD |
| 알약 값 - 걸음수 | 자릿수별 분기(28/24/21/17) — 5-4 참조 | notosanskr_bold/BOLD |
| 알약 값 - 심박수 | 19 | notosanskr_bold/BOLD |
| 알림 개수 | 11 | notosanskr_bold/BOLD |
| 콤플리케이션 "+" 플레이스홀더 | 28 | notosanskr_bold/BOLD |
| 워치이름 | 16 | **notosanskr_regular/NORMAL**(유일한 regular 사용처, 나머지 전부 bold) |

- **폰트 family 규칙**: watchface.xml 전체에서 `family="notosanskr_regular"`는 워치이름 1곳(983행)에만 쓰이고, 그 외 모든 텍스트(약 59곳)는 `family="notosanskr_bold"`, `weight="BOLD"` — 새로 만들 때도 이 원칙 그대로(워치이름만 regular, 나머지 전부 bold)

### 5-4. 걸음수 알약 값 오토핏 자릿수 분기 (x196,y354,w57,h30 공통 박스 안에서 크기만 전환)
| 자릿수 구간 | size |
|---|---|
| 100 미만 | 28 |
| 100~999 | 24 |
| 1000~9999 | 21 |
| 10000 이상(최대 999999) | 17, `isAutoSize="TRUE"` + `minSize="9"` |

- 참고: `isAutoSize`+`minSize="9"` 안전장치는 걸음수뿐 아니라 배터리(size=19)·심박수(size=19) 값 텍스트에도 동일하게 적용됨(watchface.xml 868/936/969행) — 극단적으로 긴 값이 들어와도 최소 9까지 자동 축소되는 공통 안전장치

### 5-5. 하단 알약 3개 내부 아이콘/값 절대좌표 (외곽 박스 안 세부 배치, watchface.xml 실측)
| 알약 | 아이콘(PartImage) x,y,w,h | 값(PartText) x,y,w,h |
|---|---|---|
| 배터리(외곽 65,340,96,44) | 73,362,20,14 | 93,354,68,30 |
| 걸음수(외곽 171,340,96,44) | 179,356,17,26 | 196,354,57,30(자릿수 분기 4개 공통) |
| 심박수(외곽 277,340,96,44) | 285,359,20,20 | 305,354,68,30 |

### 5-6. 날씨 아이콘 32종 x/y/width 산출 방식
- 박스 고정: x=0, y=121, width=438, height=105(전체 캔버스 폭 기준)
- 32종 전부 height=105로 고정(세로 꽉 채움), y=121 고정
- width = 원본이미지 가로/세로 비율 유지 산출: `scale = 105 / 원본높이`, `width = 원본너비 × scale`
- x = `219 − width/2` (캔버스 중앙 219 기준 좌우대칭) — 예: width104 → x=167, width159 → x≈140
- ⚠️ WFF Image는 object-fit/자동맞춤 속성이 없어서 이 계산값이 각 이미지마다 XML에 리터럴(고정 숫자)로 직접 박혀있음(런타임 계산 아님) — 새로 만들 때도 32종 각각 원본 크기 확인 후 위 공식으로 개별 x/width를 미리 계산해서 넣어야 함

### 5-1. AOD(상시화면) 전용 레이아웃
- **확정**: 위 1~17번 요소(콤플리케이션 전부 포함) 숨김, **시:분만 표시**(초 제외 — 앰비언트 1분 단위 갱신 특성상 제외 확정)

## 6. 확정 디자인 결정

### 6-1. 색상 (다수 라운드 데모 검수로 최종 확정)
- 요일 7색(월~일): #4FC3F7 #8BC34A #FFD54F #FF8A50 #F06292 #42A5F5 #EF5350
- 메인시계/콤플리케이션 공통 텍스트: 흰색(#FFFFFF) + 반투명 다크 아웃라인/그림자(rgba(0,0,0,0.6), 1~2px) — 주/야 배경 어디서나 가독성 확보
- 온도: 터콰이즈 `#26C6DA`(기존 주황에서 변경 — 일출/일몰 라벨도 warm톤이라 온도가 묻혀서 미사용 계열인 터콰이즈로 변별력 확보) + 다크 아웃라인
- 일출/일몰 통합 라벨: `#FFB199`(새벽톤)로 통일 — 기존 일몰 전용 `#C97B5F`(테라코타)는 슬롯 통합으로 폐기(더 이상 사용 안 함)
- 날짜: 골드 `#FFDDAA`(일출/일몰 라벨과 동일 계열, 시계 흰색과 구분)
- 하단 알약(배터리/걸음수/심박수) 텍스트: 밝은 민트 `#C9F7DC`, 배경 `rgba(35,26,52,.8)`+테두리 `rgba(255,255,255,.22)` 1px(주/야간 모두 경계 인식 가능하도록 충분히 불투명)
- 워치이름: 피치/탠 `#F2C9A0` + 다크 아웃라인(stroke) — 검정 단독은 야간 배경에서 가독성 저하로 폐기
- 알림 점: 주황 `#FFA726`
- 강수확률 값(%): `#4FC3F7`(요일 '월' 색상과 동일 계열 재사용) + 다크 아웃라인
- 콤플리케이션 미설정(EMPTY) 플레이스홀더: 원 `#59000000`(35% 불투명 검정), "+" 텍스트 `#B3FFFFFF`(70% 불투명 흰색)
- Scene 배경 기본색(배경 이미지 아래 깔리는 fallback): `#000000`
- 메인 시계(시:분) 다크 아웃라인 구현 방식: `TimeText`는 `Outline` 자식 요소를 지원하지 않아서, 동일 텍스트를 8방향(상하좌우+대각선) 1~2px 오프셋으로 검정색 복제 후 그 위에 흰색 원본을 덮어 그리는 방식으로 우회 구현(watchface.xml 768~823행)

### 6-1-1. 배경 그라디언트 확정값 (색감 컨셉, 실제 배경은 이 톤 기준으로 이미지 생성)
- Day: `linear-gradient(160deg, #6f52b0 0%, #9c4f9c 40%, #c05f79 68%, #cf7d5a 100%)` + 상단 하이라이트 `radial-gradient(circle at 50% 8%, rgba(255,196,130,.4), transparent 40%)`
- Night: `linear-gradient(165deg, #5c6aa0 0%, #726dae 45%, #8d6cae 75%, #5f4d85 100%)` + 별점 하이라이트 `radial-gradient(190,198,245,.4)` 계열, 작은 흰 점(별) 다수 오버레이
- 주의: Night 배경을 밝게 조정한 만큼, 흰색 텍스트 다크 아웃라인을 기존보다 진하게(예: rgba(0,0,0,0.7) 이상) 보강 검토 필요

### 6-2. 폰트 (정정됨)
- Noto Sans KR Bold 확정 — **단, 시스템 기본 폰트 아님 확인됨**(삼성 워치 One UI Watch 시스템 기본폰트는 "SamsungOne"이며 Noto Sans KR이 아님). SYNC_TO_DEVICE로 두면 기기별 시스템폰트(SamsungOne 등)로 나와 의도한 디자인과 달라짐 → **커스텀 폰트로 직접 임베딩**(`res/font/notosanskr.ttf` 등, `<Font family="notosanskr">` 방식). SIL OFL 무료 라이선스라 임베딩 배포 문제 없음

### 6-3. 시간/온도 표기
- 12시간제 + 오전/오후 접두 고정 (예: 23시 → 오후 11시)
- 오전/오후 접두 텍스트: 가로 배치 아님, **세로로 글자 쌓아서(오/전, 오/후) 메인시계 숫자 높이에 맞춤**(원본 목업 방식) — 크게, 다크 아웃라인 병행
- 오전/오후 + 초(seconds) 색상: 배경(보라~핑크/야간 톤) 어디서나 어울리는 **코럴톤 `#FFB27A` 계열로 통일**(기존 하늘색/주황 조합 폐기 — 배경과 안 어울린다는 피드백 반영)
- 메인 시계(시:분) 색상: **흰색 + 다크 아웃라인**(검정 아님 — 6-1 공통 텍스트 규칙과 통일, 주/야 배경 공통 가독성)
- 메인 시계는 레이아웃 내 최대 강조 요소(오전/오후·시분·초 세트 전체를 다른 요소보다 우선해서 크게, 단 날씨/날짜 압박 안 하는 선)
- 날짜 색상: 골드 `#FFDDAA`(일출/일몰 라벨과 동일 계열, 시계와 구분되면서 주/야 배경 모두 가독성 확보 — 검정/틸 계열은 야간 배경에서 가독성 저하로 폐기)
- 온도 단위 섭씨(℃) 고정

### 6-4. 기타
- 앱 바로가기 콤플리케이션: Wear OS 시스템 제공 "App Shortcut" 데이터소스로 통일 → 특정 앱 하드코딩 금지, 사용자가 시계에서 직접 앱 선택
- 안읽은 알림 인디케이터: 점(dot) 형태 유지, 알림 있을 때만 표시
- 날짜/시간: 기기 타임존 기준 자동 반영(해외 이동시 폰 타임존 변경 따라 자동 전환), 하드코딩 금지
- 온도/일출일몰: 위치기반(GPS) 날씨/천문 데이터소스 바인딩, 하드코딩 금지
- UI 텍스트(라벨류) 언어: 해외에서 사용해도 무조건 한글 고정 (시간/온도/날짜 값만 현지 기준으로 자동 반영, 라벨 텍스트는 번역 안 함)
- 날씨 아이콘 32종: 예비 없이 16개 코드에 1:1 전부 매핑 확정(9번, weather_icons.md 참조)

## 6-5. 콤플리케이션 슬롯 한도 (4차 정정)
- WFF 공식 스펙상 워치페이스 1개당 ComplicationSlot **최대 8개**
- 실제 필요한 ComplicationSlot은 5개(7-1 참조, 일몰 슬롯 삭제 후 축소) — 날씨(온도/아이콘)/워치배터리/걸음수/심박수는 슬롯이 아니라 직접 데이터 바인딩(7-2)이라 한도에 안 들어감, 3개 여유

## 6-6. 테스트 방법
- 1차: PC에서 Android Studio Wear OS 에뮬레이터(가상 워치)에 설치해서 확인
- 에뮬레이터는 GPS/위치 정보가 부정확해 날씨 데이터가 안 불러와질 수 있음 → 그럴 경우 빌드 결과에 "날씨 데이터 로드 실패, 실기기에서 확인 필요" 메시지로 안내
- 2차: 위 문제 발생 시 또는 최종 확인은 실제 갤럭시 워치8 클래식에 ADB(무선 디버깅)로 설치해서 테스트

## 7. 데이터 바인딩 방식 (정정됨)

### 7-1. ComplicationSlot (총 5개 — 8개 한도 중 5개 사용, 재정정됨)
- ComplicationSlot 1개 = Bounding Area 1개 필수(공식 스펙)
- ※ 이전엔 일몰/일출을 위치가 다르다는 이유로 슬롯 2개(SLOT_SUNRISE/SLOT_SUNSET) 썼으나, SUNRISE_SUNSET 시스템 데이터소스 자체가 "다음 해 이벤트 하나"만 제공해 애초에 좌우 분리 표시가 불가능했음(9-1 참조) → SLOT_SUNSET 삭제, SLOT_SUNRISE 1개로 통합(라벨만 조건부 전환)

| 슬롯ID | 위치 | 기본 데이터소스 | 사용자 변경 |
|---|---|---|---|
| SLOT_TOP_LEFT | 상단 좌 원형 | App Shortcut — **"차량"은 용도 라벨일 뿐, 특정 앱(기아 커넥트 등) 사전 지정 불가**(App Shortcut은 시스템 제공 빈 틀이라 개발자가 강제로 채울 수 없음). 최초엔 빈 상태, 사용자가 편집화면에서 직접 앱 선택해야 채워짐 | 가능 |
| SLOT_TOP_RIGHT | 상단 우 원형 | App Shortcut — "AI 어시스턴트"도 위와 동일하게 용도 라벨일 뿐, 사전 지정 불가 | 가능 |
| SLOT_MID_LEFT | 중단 좌 원형 | EMPTY(기본 빈 슬롯) — "휴대폰 배터리"는 Wear OS 시스템 기본 제공 콤플리케이션이 아님(서드파티 앱 필요 확인됨), 사용자가 앱 설치 후 직접 지정. 값 있을 때 아이콘 없이 숫자만 원형 중앙정렬, 미설정 시 원형+"+" 표시(5번 6~9 하단 참조) | 가능 |
| SLOT_MID_RIGHT | 중단 우 원형 | App Shortcut — "음성녹음"도 위와 동일하게 용도 라벨일 뿐, 사전 지정 불가(`isCustomizable` 기본값 true 유지) | 가능 |

※ App Shortcut 3개 슬롯(차량/AI/음성) 전부 특정 앱 하드코딩 불가능(6-4 원칙과 동일) — 단, 대상 앱이 자체 Wear OS 콤플리케이션 제공자(ComplicationDataSourceService)를 별도로 공개한 경우에 한해 `primaryProvider`로 직접 지정해 기본 자동표시 가능(예: 기아 커넥트가 이를 지원하는지는 미확인, 실기기에서 확인 필요)
| SLOT_SUNRISE | **좌상단**(일출/일몰 통합 텍스트) | `DefaultProviderPolicy defaultSystemProvider="SUNRISE_SUNSET"` — 공식 Wear OS 시스템 데이터소스 확인됨(Wear OS 4+, `SystemDataSources.DATA_SOURCE_SUNRISE_SUNSET`) | **`isCustomizable="false"`로 잠금 확정** — 라벨은 `WEATHER.IS_DAY` 조건부("일몰"/"일출" 전환), 값은 COMPLICATION.TEXT 그대로(2026-08-30 실기기 검증 완료) |

※ 우상단(구 SLOT_SUNSET) 자리는 삭제되어 빈 공간 — 활용 방안은 9번 참조(달의 위상 후보)

### 7-1-1. 콤플리케이션 4개 슬롯 supportedTypes + 이미지 크기/inset (원형 52x52 공통 패턴)
| 슬롯 | supportedTypes |
|---|---|
| SLOT_TOP_LEFT | MONOCHROMATIC_IMAGE, SMALL_IMAGE, EMPTY |
| SLOT_TOP_RIGHT | MONOCHROMATIC_IMAGE, SMALL_IMAGE, EMPTY |
| SLOT_MID_LEFT | SHORT_TEXT, RANGED_VALUE, MONOCHROMATIC_IMAGE, SMALL_IMAGE, EMPTY |
| SLOT_MID_RIGHT | MONOCHROMATIC_IMAGE, SMALL_IMAGE, EMPTY |

- MONOCHROMATIC_IMAGE: x=9,y=9,width=34,height=34 (52x52 원 안 inset 9px), `tintColor="#FFFFFFFF"` 적용
- SMALL_IMAGE: x=6,y=6,width=40,height=40 (inset 6px), 틴트 없음
- EMPTY 플레이스홀더: Ellipse 52x52, fill `#59000000`, "+" 텍스트 size=28, color `#B3FFFFFF`

### 7-2. 직접 데이터 바인딩 (ComplicationSlot 아님, 슬롯 한도에 포함 안 됨)
- 날씨(온도/아이콘, 일출·일몰 제외): `[WEATHER.*]` expression — WEATHER.*에는 sunrise/sunset 없음, 온도 단위 강제(℃) 가능 여부는 빌드 단계 확인
- 최고/최저 기온: `WEATHER.DAYS.0.TEMPERATURE_LOW` / `WEATHER.DAYS.0.TEMPERATURE_HIGH` 확정 사용(watchface.xml 4곳, 화씨→섭씨 변환 분기 포함) — WEATHER.DAY_TEMPERATURE_LOW/HIGH(비배열 형태)는 미사용
- 강수확률: `WEATHER.DAYS.0.CHANCE_OF_PRECIPITATION` 확정 사용(비배열 형태 WEATHER.CHANCE_OF_PRECIPITATION 아님 — 21-15단계 최초 지시 문구와 실제 구현이 다름, 실제 코드 기준으로 정정)
- 주/야간 분기: `WEATHER.IS_DAY` — 날씨 아이콘/날씨 관련 색상 등 day/night 조건분기 전반에 사용
- 온도 단위 변환: `WEATHER.TEMPERATURE_UNIT` 값으로 화씨/섭씨 분기 처리 후 필요시 변환식 적용
- 워치 배터리: `BATTERY_PERCENT` SourceType
- 걸음수: `STEP_COUNT` SourceType
- 심박수: `HEART_RATE` SourceType (WFF 자체 시스템 SourceType, 별도 SDK 연동 아님)
- 스트레스 지수: 공식 SourceType 미지원 확정(제외) — 하단 알약3은 심박수 단독, 배터리/걸음수 알약과 동일하게 아이콘+값 구성으로 채움
- 알림 개수: `UNREAD_NOTIFICATION_COUNT`
- 날짜: 시스템 날짜 필드 `YEAR`/`MONTH`/`DAY`
- 요일: 시스템 필드 `DAY_OF_WEEK`
- 오전/오후: 시스템 필드 `AMPM_STATE`
- 콤플리케이션 아이콘 틴트: `tintColor="#FFFFFFFF"`(흰색 통일) 4곳 사용 확인 — 기능상 필요(아이콘 원본 색과 무관하게 통일된 흰색으로 표시), 21-17-6 점검에서 제거 비권장 결론

## 8. 빌드/패키징 정보
- applicationId(패키지명): `com.keingma.watch` (배포 패키지명, 확정)
- namespace(코드/R클래스용): `com.galaxywatch8.watchface` (`watchface/build.gradle.kts:6`) — applicationId와 다른 값이나 Android에서 둘이 다른 건 정상(별개 용도). 단, 이 값이 스캐폴드 기본값 그대로 남은 건지 의도한 값인지는 미확인 — 문제는 없지만 참고
- rootProject.name: `GalaxyWatch8ClassicWatchFace` (`settings.gradle.kts:16`)
- AndroidManifest.xml: `<uses-feature android:name="android.hardware.type.watch" />`, `<meta-data android:name="com.google.android.wearable.standalone" android:value="true" />`, `<application android:label="@string/watch_face_name" android:icon="@drawable/preview" android:hasCode="false">`
- `strings.xml`의 `watch_face_name` = **"바람개비 워치8 클래식"**(확정, 기존 스캐폴드 기본값 "GalaxyWatch8 Classic Analog"에서 변경) — 즐겨찾기 목록에 뜨는 앱 라벨, watchface.xml 내부 렌더링 텍스트(5번-16)와 동일 문자열로 통일
- minSdk 34 (WFF v2 요구사항) 고정 — 33이면 WFF v2 최소요구 미달이라 34로 수정 필요
- targetSdk/compileSdk: 재빌드 시점마다 최신 버전으로 갱신(현재 36 제안), minSdk는 유지
- versionCode 1 / versionName "1.0" (시작값, "1.0.0" 아님)
- keystore(서명): 지금 정할 필요 없음 — 실제 빌드 단계에서 생성
- 런처 아이콘(워치페이스 목록 썸네일): 완료 — 22단계로 fast-track 처리됨, `watchface/src/main/res/drawable/preview.png`(실제 워치페이스 캡처, 래스터) + `watch_face_info.xml`의 `<Preview value="@drawable/preview" />` 등록 완료
- `watch_face_info.xml`의 `<Editable value="true" />` 필수(기본값 false — 없으면 편집화면 진입 불가, 21-16-3 확인)
- AndroidManifest.xml `android:hasCode="false"` 필수(WFF는 코드 없이 리소스만 포함하는 게 정상)
- watch_face_info.xml 기타 확정값: `<Category value="CATEGORY_EMPTY" />`, `<AvailableInRetail value="false" />`, `<MultipleInstancesAllowed value="false" />`, `<FlavorsSupported value="false" />`
- release 빌드타입: `isMinifyEnabled=false`, `isShrinkResources=false`

## 9. 미확정/보류 항목
- 날씨 아이콘 32종 전부 매핑 완료(예비 없음, `docs/weather_icons.md` 참조) — 단 FOG/MIST/LIGHT_SNOW/UNKNOWN 4개 코드는 원래 컨셉과 다른 그림을 근사치로 재배정한 것이라 실제 렌더 확인 시 어색하면 재조정 가능
- 걸음수/심박수: WFF 자체 SourceType으로 제공되며 삼성헬스 SDK를 직접 연동하지 않음. 권한 요구 여부 및 처리 방식은 구현/실기기 테스트에서 확인. 스트레스 지수만 시스템 콤플리케이션/우회 존재 여부 실기기 테스트에서 확인
- AOD 초(seconds) 표시: 제외로 확정(구글 공식 원칙상 ambient 1분 단위 갱신 — 5-1 참조)
- **우상단 빈 공간(구 일몰 슬롯 자리) 활용 미정**: 달의 위상(MOON_PHASE_POSITION/TYPE) 표시 후보 — Samsung 공식 코드랩 확인 결과 이미지 2장(보름달 계열/초승달 계열)+마스크 기법으로 8단계 위상 전부 표현 가능(32종 날씨 아이콘 대비 훨씬 적은 리소스). 단, WFS(GUI 도구) 기준 설명이라 raw WFF XML(Mask 요소 문법)로 직접 구현해야 함 — 시간 날 때 진행 예정. 그 전까지는 빈 공간 유지
- **콤플리케이션 개별 탭→앱 선택 피커**: 에뮬레이터(uiautomator)에서는 개별 탭 타겟 노출이 안 돼서 확인 불가(실제 미구현인지 AVD 한계인지 불명확) — 실기기에서 최종 확인 필요(개발_지시서.md 12단계)
- **실기기 가독성 문제(2026-08-30 발견)**: 실물 워치에서 요일/알약 등 작은 텍스트가 사용자 기준 잘 안 보임(노안 등 개인 시력 특성 — 폰트 크기 자체를 전반적으로 상향 조정 검토 필요). 즉시 수정 아님, 추후 시간 날 때 전체 폰트 크기 재검토 예정. 기준 스크린샷은 세션 로컬 보관 중.

## 9-1. 확정 버그/오해 정정 기록 (재발 방지용)
- **WFF에는 `<Outline>` 요소가 존재하지 않음**: 한때 `<Font><Outline color="..." width="..."/>텍스트</Font>` 구문이 공식 지원되는 것으로 오인하여 요일 텍스트에 적용했으나, 실기기·에뮬레이터 둘 다 아무 효과 없이 무시됨(파서가 미지원 요소를 조용히 스킵). 공식 "Work with text" 튜토리얼 재확인 결과 텍스트 데코레이션은 `OutGlow`/`Shadow`만 존재. **텍스트 아웃라인(테두리)은 항상 TimeText와 동일한 방식(검정색 복제 텍스트를 8방향 오프셋으로 원본 색상 텍스트 아래 겹쳐 그리기)으로만 구현할 것** — `<Outline>` 요소 시도 금지.
- 요일 텍스트 아웃라인: 검정 `#D9000000`, 8방향 ±2px 오프셋 복제 PartText 8개 + 원본 색상 PartText 1개(총 9개 겹침) 구조로 확정(2026-08-30 실기기 검증 완료).
- **SUNRISE_SUNSET 시스템 데이터소스는 "다음 해 이벤트 하나"만 제공(일출 전용/일몰 전용으로 나눠 받는 기능 없음)**: Samsung 개발자 공식 답변으로 확인("Sunrise / Sunset tells that it is either when the next sunset will be or the next Sunrise"). 낮(일출~일몰)엔 다음 일몰 시각을, 밤(일몰~다음일출)엔 다음 일출 시각을 자동으로 반환하는 단일 값 구조. **두 슬롯에 각각 넣어 좌우 분리 표시하는 설계는 애초에 불가능** — 처음부터 다시 만들 때는 슬롯 1개(SLOT_SUNRISE)만 만들고 라벨을 `WEATHER.IS_DAY`로 조건부 전환할 것(7-1 참조), 2슬롯 설계 반복 금지.

## 10. 작업 방식 규칙 (Claude Code CLI 대상)
- 요청 범위 외 리팩토링 금지
- 답변은 한글, 토큰 최소화, 수다/설명 금지(코드 생성 불가 시에만 1줄 원인)
- 정상 진행 중에는 중간 멘트/진행상황 설명 출력하지 말 것 — 작업 끝나면 PROGRESS.md만 갱신하고 조용히 종료
- 메시지 출력은 다음 경우에만: 에러 발생, 사용자 확인/결정 필요, 작업 불가
- 빌드/테스트 로그는 성공시 1줄 요약만, 실패시 원인 부분만 출력(전체 로그 금지)
- git diff/grep 등 조회 결과도 전체 대신 관련 부분만 출력
- 이미지/바이너리 파일 내용을 텍스트(base64 등)로 출력 금지
- 커밋 메시지는 1줄로 간결하게
- 개발_지시서.md에 이미 있는 계획/맥락을 다시 설명하지 말고 바로 실행
- 코드 수정 시 전체 파일/diff 출력 금지 — 수정된 함수 단위(불가 시 클래스 단위)만 출력
- 주석은 UI성 주석 금지, DB/권한/상태 등 핵심 로직만 1줄 개조식 한글 주석
- 리소스 경로는 4번 기준 고정 — 매번 위치 재확인 지시 불필요
- 사용자가 "N단계 진행해" 라고 하면 `docs/개발_지시서.md`의 N단계 섹션 프롬프트 내용을 그대로 수행하고, 끝나면 그 섹션에 명시된 `PROGRESS.md` 체크박스를 갱신할 것

## 11. 참고 출처
- [Watch Face Format | Android Developers](https://developer.android.com/training/wearables/wff)
- [Optimize your watch face design | Samsung Developer](https://developer.samsung.com/codelab/watch-face-studio/design-optimization.html)
- [Always-on in Watch Face Studio | Samsung Developer](https://developer.samsung.com/watch-face-studio/user-guide/always-on.html)
- [Samsung Galaxy Watch8 Classic - GSMArena](https://www.gsmarena.com/samsung_galaxy_watch8_classic-13998.php)
- [Set up a watch face project (Editable/Preview) | Android Developers](https://developer.android.com/training/wearables/wff/setup)
- [Weather data in the Watch Face Format | Android Developers](https://developer.android.com/training/wearables/wff/weather)
- [Optimize memory usage | Android Developers](https://developer.android.com/training/wearables/wff/memory-usage)
- [Optimize watch face performance | Android Developers](https://developer.android.com/training/wearables/watch-faces/performance)
- [google/watchface (Validator, WFF Optimizer) | GitHub](https://github.com/google/watchface)
