-- 테스트용 User 데이터
INSERT INTO users (name, job_category, career_stage, preparation_method, created_at, updated_at)
VALUES ('김철수', 'DEV', 'NEW', '코딩 테스트, 면접 스터디', NOW(), NOW());

INSERT INTO users (name, job_category, career_stage, preparation_method, created_at, updated_at)
VALUES ('이영희', 'DESIGN', 'JUNIOR', '포트폴리오 제작', NOW(), NOW());

-- 테스트용 Application 데이터
INSERT INTO application (user_id, company_name, job_title, interview_date, failed_stage, simple_memo, reflection_status, created_at, updated_at)
VALUES (1, '토스', 'Backend Developer', '2024-02-01', '서류', '포트폴리오에 집중했음', '전', NOW(), NOW());

INSERT INTO application (user_id, company_name, job_title, interview_date, failed_stage, simple_memo, reflection_status, created_at, updated_at)
VALUES (1, '카카오', 'Backend Developer', '2024-01-15', '1차 면접', '기술 면접 준비', '전', NOW(), NOW());

INSERT INTO application (user_id, company_name, job_title, interview_date, failed_stage, simple_memo, reflection_status, created_at, updated_at)
VALUES (2, '네이버', 'Product Designer', '2024-02-05', '포트폴리오', 'UX 케이스 스터디 작성', '전', NOW(), NOW());
