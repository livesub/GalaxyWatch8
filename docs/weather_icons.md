# 날씨 아이콘 리소스 (32종)

## 파일명 규칙
`{day|night}_{번호2자리}_{설명}.png` (원본 480x480 RGBA, 실사용 전 WebP 변환+리사이즈 필수 — CLAUDE.md 3-4 참조)

## day (16개)
day_01_clear_sun, day_02_partly_cloudy, day_03_cloud_double, day_04_clear_sun_b,
day_05_rain_light, day_06_rain_beanie_sad, day_07_snow_light, day_08_snow_beanie_happy,
day_09_thunderstorm, day_10_cloudy_plain, day_11_wind_light, day_12_wind_strong,
day_13_rain_crying, day_14_cloud_dark_angry, day_15_rain_beanie_happy, day_16_snow_beanie_angry

## night (16개)
night_01_clear_moon, night_02_partly_cloudy_moon, night_03_cloudy_plain, night_04_stars_clear,
night_05_rain_light, night_06_rain_sad, night_07_snow_light, night_08_snow_beanie_happy,
night_09_thunderstorm_angry, night_10_cloud_plain2, night_11_wind_light, night_12_wind_strong,
night_13_rain_crying, night_14_cloud_angry, night_15_rain_beanie_happy, night_16_snow_beanie_angry2

## WEATHER.CONDITION 매핑 (WFF 공식 0~15, day/night는 WEATHER.IS_DAY로 분기)
- **32종 전부 1:1 매핑 확정**(예비/미사용 없음) — 코드 16개=이미지 16개, 겹치지 않게 재배정
| 코드 | 공식 이름 | day 파일 | night 파일 | 비고 |
|---|---|---|---|---|
| 0 | UNKNOWN_VALUE | day_15_rain_beanie_happy | night_15_rain_beanie_happy | 실사용 거의 안 되는 코드라 남는 그림 배정(임의) |
| 1 | CLEAR | day_01_clear_sun | night_01_clear_moon | |
| 2 | CLOUDY | day_10_cloudy_plain | night_10_cloud_plain2 | |
| 3 | FOG | day_14_cloud_dark_angry | night_14_cloud_angry | 짙은 안개=어둡고 탁한 구름으로 표현(근사치) |
| 4 | HEAVY_RAIN | day_13_rain_crying | night_13_rain_crying | |
| 5 | HEAVY_SNOW | day_16_snow_beanie_angry | night_16_snow_beanie_angry2 | |
| 6 | RAIN | day_06_rain_beanie_sad | night_06_rain_sad | |
| 7 | SNOW | day_08_snow_beanie_happy | night_08_snow_beanie_happy | |
| 8 | SUNNY | day_04_clear_sun_b | night_04_stars_clear | CLEAR와 구분(맑음 2번째 버전) |
| 9 | THUNDERSTORM | day_09_thunderstorm | night_09_thunderstorm_angry | |
| 10 | SLEET | day_07_snow_light | night_07_snow_light | |
| 11 | LIGHT_SNOW | day_12_wind_strong | night_12_wind_strong | "바람에 날리는 눈발" 컨셉으로 재해석(근사치) |
| 12 | LIGHT_RAIN | day_05_rain_light | night_05_rain_light | |
| 13 | MIST | day_03_cloud_double | night_03_cloudy_plain | 옅은 안개=구름 2겹 흐릿함으로 표현(근사치) |
| 14 | PARTLY_CLOUDY | day_02_partly_cloudy | night_02_partly_cloudy_moon | |
| 15 | WINDY | day_11_wind_light | night_11_wind_light | |

## 날씨 코멘트 매핑 (온도 아래 표시할 한글 1줄, CLAUDE.md 5번-3 참조)
| 코드 | 공식 이름 | 코멘트 |
|---|---|---|
| 0 | UNKNOWN_VALUE | 정보 없음 |
| 1 | CLEAR | 맑음 |
| 2 | CLOUDY | 흐림 |
| 3 | FOG | 안개 |
| 4 | HEAVY_RAIN | 폭우 |
| 5 | HEAVY_SNOW | 폭설 |
| 6 | RAIN | 비 |
| 7 | SNOW | 눈 |
| 8 | SUNNY | 화창함 |
| 9 | THUNDERSTORM | 천둥번개 |
| 10 | SLEET | 진눈깨비 |
| 11 | LIGHT_SNOW | 가끔 눈 |
| 12 | LIGHT_RAIN | 가끔 비 |
| 13 | MIST | 옅은 안개 |
| 14 | PARTLY_CLOUDY | 대체로 흐림 |
| 15 | WINDY | 바람 강함 |

예비 자원 없음 — 32종 전부 매핑에 사용됨(위 표 참조)

출처: [Weather data in the Watch Face Format](https://developer.android.com/training/wearables/wff/weather), [Data sources reference](https://developer.android.com/training/wearables/wff/common/attributes/source-type)
