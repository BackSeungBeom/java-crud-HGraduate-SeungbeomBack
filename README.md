# HGraduate

수강 과목(Course) 관리 CRUD 콘솔 프로그램. 동일한 `CourseRepository` 인터페이스를 메모리, 파일, MariaDB 세 가지 방식으로 구현하며 CRUD와 JDBC를 학습하기 위한 프로젝트입니다.

## 기술 스택

- Java 21
- Maven
- MariaDB (Docker Compose로 실행)
- mariadb-java-client (JDBC)
- JUnit 5

## 폴더 구조

```
src/main/java/org/hgraduate
├── HGraduateApplication.java   # 진입점 (main)
├── model/                      # Course, Category, Status
├── repository/                 # CourseRepository 인터페이스 + 3가지 구현체
│   ├── CourseMemoryRepository.java   # 메모리 기반
│   ├── CourseFileRepository.java     # 파일(csv) 기반
│   └── CourseDbRepository.java       # MariaDB 기반
├── service/CourseService.java  # 비즈니스 로직 (중복 체크 등)
├── view/ConsoleView.java       # 콘솔 입출력
├── util/DBConnection.java      # DB 연결 싱글톤
└── exception/                  # 커스텀 예외

src/main/resources
├── db.properties.example       # DB 접속 정보 템플릿
└── db.properties               # 실제 접속 정보 (git에는 올라가지 않음)

compose.yaml                    # MariaDB Docker 실행 설정
```

## DB 실행 (Docker Compose)

이 프로젝트는 MariaDB를 Docker 컨테이너로 띄워서 사용합니다.

```bash
docker compose up -d
```

`compose.yaml` 기준 접속 정보:

| 항목 | 값 |
|---|---|
| Host | localhost |
| Port | 3308 |
| Database | crud_db |
| User | user |
| Password | password |

컨테이너가 처음 뜨면 DB는 비어있으므로, `course` 테이블을 직접 생성해야 합니다.

```sql
CREATE TABLE course (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_code VARCHAR(100) NOT NULL UNIQUE,
    course_name VARCHAR(100) NOT NULL,
    category ENUM('MAJOR_REQUIRED', 'MAJOR_ELECTIVE', 'GENERAL_REQUIRED', 'GENERAL_ELECTIVE', 'FREE_ELECTIVE') NOT NULL,
    credit INT NOT NULL,
    semester VARCHAR(100) NOT NULL,
    status ENUM('COMPLETED', 'PLANNED', 'RETAKE') NOT NULL,
    grade VARCHAR(100),
    created_at DATETIME NOT NULL
);
```

## 실행 방법

1. `src/main/resources/db.properties.example`을 복사해 `db.properties`를 만들고, 위 DB 접속 정보를 채워 넣습니다.

   ```properties
   db.url=jdbc:mariadb://localhost:3308/crud_db
   db.username=user
   db.password=password
   db.driver=org.mariadb.jdbc.Driver
   ```

2. Docker로 DB를 띄웁니다.

   ```bash
   docker compose up -d
   ```

3. `HGraduateApplication`의 `main()`을 IDE에서 실행합니다.

