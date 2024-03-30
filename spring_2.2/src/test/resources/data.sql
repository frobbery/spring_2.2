INSERT INTO AUTHOR (FULL_NAME) VALUES ('Pushkin'), ('Rubina');
INSERT INTO BOOK (NAME, AUTHOR_ID) VALUES ('Regular adventure novel', 1), ('Regular romance novel', 2), ('Romance-adventure novel', 2);
INSERT INTO GENRE (GENRE_NAME) VALUES ('Adventure'), ('Romance');
INSERT INTO BOOK_GENRE (BOOK_ID, GENRE_ID) VALUES (1, 1), (2, 2), (3, 1), (3, 2);
INSERT INTO COMMENT (BOOK_ID, COMMENT_TEXT) VALUES (1, 'GOOD'), (1, 'BAD');