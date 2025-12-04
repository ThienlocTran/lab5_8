-- Bước 1: Tạo cơ sở dữ liệu (Database)
-- Bạn có thể bỏ qua bước này nếu đã có DB và chỉ muốn tạo bảng trong DB đó.
-- USE master;
-- CREATE DATABASE PolyOE;
-- DROP DATABASE PolyOE;
-- Sử dụng Database vừa tạo
USE PolyOE;
SELECT @@SERVERNAME
SELECT @@VERSION

DROP TABLE IF EXISTS Logs;
DROP TABLE IF EXISTS Share;
DROP TABLE IF EXISTS Favorite;
DROP TABLE IF EXISTS Video;
DROP TABLE IF EXISTS [User];
DROP TABLE IF EXISTS VISITOR;

CREATE TABLE VISITOR(
    Id Int IDENTITY(1,1) PRIMARY KEY
    ,VisitorCount Int NOT NULL DEFAULT 0
)

CREATE TABLE Logs(
    Id INT IDENTITY(1,1) PRIMARY KEY,
    Url NVARCHAR(MAX) NOT NULL,
    [Time] DATETIME NOT NULL,
    Username NVARCHAR(50)
)
-- Bước 2: Tạo Bảng User
CREATE TABLE [User] (
    id_User NVARCHAR(50) PRIMARY KEY, -- PK
    password_User NVARCHAR(50) NOT NULL,
    email NVARCHAR(50) NOT NULL UNIQUE,
    fullName NVARCHAR(50) NOT NULL,
    Admin_User BIT NOT NULL DEFAULT 0 -- Admin là kiểu Boolean trong ERD, tương đương BIT trong SQL Server
);

-- Bước 3: Tạo Bảng Video
CREATE TABLE Video (
    Id NVARCHAR(50) PRIMARY KEY, -- PK
    Title NVARCHAR(255) NOT NULL,
    Poster NVARCHAR(255),
    Views INT NOT NULL DEFAULT 0, -- Views là kiểu Integer
    [Description] NVARCHAR(MAX),
    Active BIT NOT NULL DEFAULT 1 -- Active là kiểu Boolean, tương đương BIT
);

-- Bước 4: Tạo Bảng Favorite (Sở thích/Yêu thích)
CREATE TABLE Favorite (
    Id BIGINT IDENTITY(1,1) PRIMARY KEY, -- Id là kiểu Long trong ERD, sử dụng BIGINT và IDENTITY để tự tăng
    UserId NVARCHAR(50) NOT NULL, -- FK
    VideoId NVARCHAR(50) NOT NULL, -- FK
    LikeDate DATE NOT NULL,
    CONSTRAINT FK_Favorite_User FOREIGN KEY (UserId) REFERENCES [User](id_User),
    CONSTRAINT FK_Favorite_Video FOREIGN KEY (VideoId) REFERENCES Video(Id)
);

-- Bước 5: Tạo Bảng Share (Chia sẻ)
CREATE TABLE Share (
    Id BIGINT IDENTITY(1,1) PRIMARY KEY, -- Id là kiểu Long, sử dụng BIGINT và IDENTITY để tự tăng
    UserId NVARCHAR(50) NOT NULL, -- FK
    VideoId NVARCHAR(50) NOT NULL, -- FK
    Emails NVARCHAR(MAX) NOT NULL, -- Emails là kiểu String, sử dụng NVARCHAR(MAX) vì có thể lưu nhiều email
    ShareDate DATE NOT NULL,
    CONSTRAINT FK_Share_User FOREIGN KEY (UserId) REFERENCES [User](id_User),
    CONSTRAINT FK_Share_Video FOREIGN KEY (VideoId) REFERENCES Video(Id)
);

-- ========== THÊM DỮ LIỆU ẢO ==========

-- Bước 6: Thêm dữ liệu User (6 người dùng)
INSERT INTO [User] (id_User, password_User, email, fullName, Admin_User) VALUES
('U001', 'pass123', 'john.doe@gmail.com', 'John Doe', 1),
('U002', 'pass456', 'jane.smith@gmail.com', 'Jane Smith', 0),
('U003', 'pass789', 'bob.wilson@gmail.com', 'Bob Wilson', 0),
('U004', 'pass012', 'alice.johnson@gmail.com', 'Alice Johnson', 0),
('U005', 'pass345', 'charlie.brown@gmail.com', 'Charlie Brown', 0),
('U006', 'pass678', 'diana.prince@gmail.com', 'Diana Prince', 1);

-- Bước 7: Thêm dữ liệu Video (20 video)
INSERT INTO Video (Id, Title, Poster, Views, [Description], Active) VALUES
('V001', 'Learning Java Basics', 'java1.jpg', 1500, 'Complete guide to Java programming fundamentals', 1),
('V002', 'Advanced SQL Queries', 'sql1.jpg', 2300, 'Master complex SQL queries and optimization', 1),
('V003', 'Web Development with HTML CSS', 'web1.jpg', 1800, 'Build modern websites with responsive design', 1),
('V004', 'JavaScript ES6 Tutorial', 'js1.jpg', 2100, 'Learn modern JavaScript features and best practices', 1),
('V005', 'Database Design Patterns', 'db1.jpg', 950, 'Understanding relational database design', 1),
('V006', 'React.js for Beginners', 'react1.jpg', 3200, 'Start building UIs with React library', 1),
('V007', 'Spring Boot Microservices', 'spring1.jpg', 1700, 'Build scalable microservices with Spring Boot', 1),
('V008', 'Docker & Kubernetes Basics', 'docker1.jpg', 2050, 'Containerization and orchestration fundamentals', 1),
('V009', 'Python Data Science', 'python1.jpg', 2800, 'Data analysis and visualization with Python', 1),
('V010', 'Git Version Control', 'git1.jpg', 1200, 'Master Git and GitHub workflows', 1),
('V011', 'RESTful API Design', 'api1.jpg', 1900, 'Design and build robust REST APIs', 1),
('V012', 'Machine Learning Introduction', 'ml1.jpg', 2600, 'Getting started with machine learning algorithms', 1),
('V013', 'Angular Framework Guide', 'angular1.jpg', 1400, 'Build dynamic web applications with Angular', 1),
('V014', 'Cloud Computing with AWS', 'aws1.jpg', 2200, 'Deploy applications on Amazon Web Services', 1),
('V015', 'TypeScript Advanced Concepts', 'ts1.jpg', 1550, 'Deep dive into TypeScript language features', 1),
('V016', 'Mobile App Development', 'mobile1.jpg', 1850, 'Create cross-platform mobile applications', 1),
('V017', 'Cybersecurity Fundamentals', 'security1.jpg', 1300, 'Essential security concepts and practices', 1),
('V018', 'DevOps Best Practices', 'devops1.jpg', 1650, 'Implementing continuous integration and deployment', 1),
('V019', 'GraphQL API Development', 'graphql1.jpg', 1100, 'Building efficient APIs with GraphQL', 1),
('V020', 'System Design Patterns', 'system1.jpg', 2400, 'Architectural patterns for scalable systems', 1);

-- Bước 8: Thêm dữ liệu Favorite (25+ yêu thích, có cả video được yêu thích và không)
INSERT INTO Favorite (UserId, VideoId, LikeDate) VALUES
('U001', 'V001', '2023-01-15'),
('U001', 'V002', '2023-02-20'),
('U001', 'V004', '2023-03-10'),
('U001', 'V006', '2023-06-05'),
('U001', 'V012', '2024-01-12'),
('U002', 'V003', '2023-04-08'),
('U002', 'V005', '2023-05-15'),
('U002', 'V009', '2023-07-22'),
('U002', 'V011', '2024-02-14'),
('U002', 'V020', '2024-09-10'),
('U003', 'V007', '2023-03-25'),
('U003', 'V008', '2023-08-30'),
('U003', 'V010', '2023-09-05'),
('U003', 'V014', '2024-03-18'),
('U003', 'V019', '2024-11-01'),
('U004', 'V001', '2023-02-10'),
('U004', 'V013', '2023-10-12'),
('U004', 'V016', '2024-04-20'),
('U004', 'V017', '2024-07-15'),
('U005', 'V002', '2023-05-30'),
('U005', 'V015', '2024-01-25'),
('U005', 'V018', '2024-08-22'),
('U006', 'V004', '2023-11-08'),
('U006', 'V009', '2024-02-28'),
('U006', 'V012', '2024-10-05');

-- Bước 9: Thêm dữ liệu Share (chia sẻ video với các thời gian khác nhau từ 2023 đến hiện tại)
INSERT INTO Share (UserId, VideoId, Emails, ShareDate) VALUES
('U001', 'V001', 'friend1@gmail.com, friend2@gmail.com', '2023-01-20'),
('U001', 'V006', 'colleague@gmail.com, manager@gmail.com', '2023-06-10'),
('U001', 'V012', 'team@gmail.com', '2024-01-15'),
('U002', 'V003', 'student1@gmail.com, student2@gmail.com, student3@gmail.com', '2023-04-15'),
('U002', 'V009', 'partner@gmail.com', '2023-07-25'),
('U002', 'V020', 'group@gmail.com', '2024-09-12'),
('U003', 'V007', 'dev.team@gmail.com', '2023-03-30'),
('U003', 'V008', 'trainee@gmail.com, junior@gmail.com', '2023-09-10'),
('U003', 'V010', 'all@company.com', '2024-03-20'),
('U004', 'V001', 'sister@gmail.com, brother@gmail.com', '2023-02-15'),
('U004', 'V013', 'mentor@gmail.com', '2023-10-15'),
('U004', 'V016', 'friends@gmail.com', '2024-04-25'),
('U004', 'V017', 'security.team@gmail.com', '2024-07-18'),
('U005', 'V002', 'colleague1@gmail.com, colleague2@gmail.com', '2023-06-05'),
('U005', 'V015', 'typescript.group@gmail.com', '2024-01-28'),
('U005', 'V018', 'devops.team@gmail.com', '2024-08-25'),
('U006', 'V004', 'javascript.enthusiasts@gmail.com', '2023-11-10'),
('U006', 'V009', 'ml.community@gmail.com', '2024-03-05'),
('U006', 'V012', 'ai.researchers@gmail.com', '2024-10-08'),
('U001', 'V002', 'admin.team@gmail.com', '2024-11-15');