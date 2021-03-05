CREATE TABLE class(
    id SERIAL NOT NULL PRIMARY KEY,
    class_code VARCHAR(50) NOT NULL,
    class_name VARCHAR(150) NOT NULL
);

CREATE TABLE statistic(
    id SERIAL NOT NULL PRIMARY KEY ,
    class_id SERIAL NOT NULL UNIQUE,
    review_count VARCHAR(50) NOT NULL,
    difficulty VARCHAR(50) NOT NULL,
    workload_avg VARCHAR(50) NOT NULL,
    rating VARCHAR(50) NOT NULL,
    FOREIGN KEY (class_id) REFERENCES class(id)
);


--/ Initial entries
INSERT INTO class(class_code, class_name)
VALUES('CIS-515', 'Fundamentals of Linear Algebra & Optimization');

INSERT INTO statistic(class_id, review_count, difficulty, workload_avg, rating)
VALUES(1, '2', '3.5', '15', '5');