REM *********************************************
CREATE TABLE rev_cat(
    r_cat_id    INTEGER      NOT NULL,
    r_cat_name  VARCHAR(20)  NOT NULL
);
CREATE UNIQUE INDEX r_cat_id_pk ON rev_cat(r_cat_id);
ALTER TABLE rev_cat ADD(CONSTRAINT r_cat_id_pk PRIMARY KEY (r_cat_id));

REM *********************************************
CREATE SEQUENCE rev_cat_id_seq
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

REM *********************************************
CREATE TABLE revenue(
    rev_id  INTEGER       NOT NULL,
    value   DECIMAL(19,4) NOT NULL,
    amount  DECIMAL(19,4) NOT NULL,
    unit    CHAR(2)     NOT NULL,
    year    INTEGER     NOT NULL,
    month   INTEGER     NOT NULL,
    day     INTEGER     NOT NULL,
    name    VARCHAR(20) NOT NULL,
    r_cat_id  INTEGER     NOT NULL
);
CREATE UNIQUE INDEX rev_id_pk ON revenue(rev_id);
ALTER TABLE revenue ADD(CONSTRAINT rev_id_pk PRIMARY KEY (rev_id),
                        CONSTRAINT r_cat_id_fk FOREIGN KEY (r_cat_id) REFERENCES rev_cat(r_cat_id));

REM *********************************************
CREATE SEQUENCE rev_id_seq
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

REM *********************************************
CREATE TABLE exp_cat(
    e_cat_id    INTEGER      NOT NULL,
    e_cat_name  VARCHAR(20)  NOT NULL
);
CREATE UNIQUE INDEX e_cat_id_pk ON exp_cat(e_cat_id);
ALTER TABLE exp_cat ADD(CONSTRAINT e_cat_id_pk PRIMARY KEY (e_cat_id));
REM *********************************************
CREATE SEQUENCE exp_cat_id_seq
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

REM *********************************************
CREATE TABLE expense(
    exp_id  INTEGER       NOT NULL,
    value   DECIMAL(19,4) NOT NULL,
    amount  DECIMAL(19,4) NOT NULL,
    unit    CHAR(2)     NOT NULL,
    year    INTEGER     NOT NULL,
    month   INTEGER     NOT NULL,
    day     INTEGER     NOT NULL,
    name    VARCHAR(20) NOT NULL,
    e_cat_id  INTEGER     NOT NULL
);
CREATE UNIQUE INDEX exp_id_pk ON expense(exp_id);
ALTER TABLE expense ADD(CONSTRAINT exp_id_pk PRIMARY KEY (exp_id),
                        CONSTRAINT e_cat_id_fk FOREIGN KEY (e_cat_id) REFERENCES exp_cat(e_cat_id));
REM *********************************************
CREATE SEQUENCE exp_id_seq
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

REM *********************************************
CREATE TABLE summary(
    year    INTEGER       NOT NULL,
    month   INTEGER       NOT NULL,
    balance DECIMAL(19,4) NOT NULL,
    
    CONSTRAINT summary_pk PRIMARY KEY (year, month)
);

REM *********************************************
COMMIT;
REM *********************************************
REM TABLES AND SEQUENCES DONE
REM *********************************************
COMMENT ON TABLE rev_cat IS 'Contains unique indices and names for revenue categories.';
COMMENT ON COLUMN rev_cat.r_cat_id IS 'Unique index for every category.';
COMMENT ON COLUMN rev_cat.r_cat_name IS 'Name of given category';

COMMENT ON TABLE revenue IS 'Contains revenue.';
COMMENT ON COLUMN revenue.rev_id IS 'Unique index for every entry.';
COMMENT ON COLUMN revenue.value IS 'Value of the items in revenue entry.';
COMMENT ON COLUMN revenue.amount IS 'Amount of the items in revenue entry.';
COMMENT ON COLUMN revenue.unit IS 'Unit of given entry ex.kg.';
COMMENT ON COLUMN revenue.year IS 'The year when entry was added..';
COMMENT ON COLUMN revenue.month IS 'The month when entry was added.';
COMMENT ON COLUMN revenue.day IS 'The day when entry was added.';
COMMENT ON COLUMN revenue.name IS 'Name of given revenue entry.';
COMMENT ON COLUMN revenue.r_cat_id IS 'Unique index of category form rev_cat table.';

COMMENT ON TABLE exp_cat IS 'Contains unique indices and names for expense categories.';
COMMENT ON COLUMN exp_cat.e_cat_id IS 'Unique index for every category.';
COMMENT ON COLUMN exp_cat.e_cat_name IS 'Name of given category';

COMMENT ON TABLE expense IS 'Contains expenses.';
COMMENT ON COLUMN expense.exp_id IS 'Unique index for every entry.';
COMMENT ON COLUMN expense.value IS 'Value of the items in expense entry.';
COMMENT ON COLUMN expense.amount IS 'Amount of the items in expense entry.';
COMMENT ON COLUMN expense.unit IS 'Unit of given entry ex.kg.';
COMMENT ON COLUMN expense.year IS 'The year when entry was added..';
COMMENT ON COLUMN expense.month IS 'The month when entry was added.';
COMMENT ON COLUMN expense.day IS 'The day when entry was added.';
COMMENT ON COLUMN expense.name IS 'Name of given expense entry.';
COMMENT ON COLUMN expense.e_cat_id IS 'Unique index of category form exp_cat table.';

COMMENT ON TABLE summary IS 'Contains a monthly summaries over expenses and revenues.';
COMMENT ON COLUMN summary.year IS 'Year of summary.';
COMMENT ON COLUMN summary.month IS 'Month of summary that summarises the turnover.';
COMMENT ON COLUMN summary.balance IS 'CURRENT balance of home budget updated with every entry.';

REM *********************************************
REM COMMENTS FOR TABELS
REM *********************************************
CREATE OR REPLACE PROCEDURE insert_rev
    (p_value revenue.value%type,
     p_amount revenue.amount%type,
     p_unit revenue.unit%type,
     p_year revenue.year%type,
     p_month revenue.month%type,
     p_day revenue.day%type,
     p_name revenue.name%type,
     p_cat_name IN VARCHAR)
    IS
     v_cat_name rev_cat.r_cat_name%TYPE;
     v_cat_id rev_cat.r_cat_id%TYPE;
     
    test_val NUMBER;
    BEGIN
    
    SELECT COUNT(*) INTO test_val FROM rev_cat WHERE r_cat_name = p_cat_name AND rownum = 1;
    IF test_val = 1 THEN
        SELECT r_cat_id INTO v_cat_id FROM rev_cat WHERE r_cat_name = p_cat_name;
    ELSE
        INSERT INTO rev_cat
        (r_cat_id, r_cat_name)
        VALUES
        (rev_cat_id_seq.NEXTVAL, p_cat_name);
        COMMIT;
        SELECT r_cat_id INTO v_cat_id FROM rev_cat WHERE r_cat_name = p_cat_name;
    END IF;
    
    INSERT INTO revenue
    (rev_id, value, amount, unit, year, month, day, name, r_cat_id)
    VALUES
    (rev_id_seq.NEXTVAL, p_value, p_amount, p_unit, p_year, p_month, p_day, p_name, v_cat_id);
END insert_rev;
/
REM *********************************************
CREATE OR REPLACE TRIGGER insert_rev_trig
AFTER INSERT ON revenue
    FOR EACH ROW
    DECLARE
        val      INTEGER;
        val_mon  INTEGER;
        val_year INTEGER;
        test_val INTEGER;
        test_val2 INTEGER;
        prev_sum INTEGER;
    BEGIN
        --Get values of new insert
        val := :NEW.value;
        val_mon := :NEW.month;
        val_year := :NEW.year;
        --Check if the summary entry for given month exist
        SELECT COUNT(*) INTO test_val FROM summary WHERE val_mon = month AND val_year = year AND rownum = 1;
        IF test_val = 0 THEN
            SELECT COUNT(*) INTO test_val2 FROM summary WHERE (month < val_mon AND year = val_year) OR (year < val_year);
                IF test_val2 != 0 THEN
                    SELECT balance INTO prev_sum FROM
                    (SELECT * FROM summary WHERE (month < val_mon AND year = val_year) OR (year < val_year) ORDER BY year DESC, month DESC)
                    WHERE ROWNUM = 1;
                ELSE
                    prev_sum := 0;
                END IF;
            INSERT INTO summary(year, month, balance)
            VALUES(val_year, val_mon, prev_sum);
        END IF;
        UPDATE summary SET balance = balance+val WHERE month >= val_mon OR year > val_year;
END;
/
REM *********************************************
CREATE OR REPLACE PROCEDURE insert_exp
    (p_value expense.value%type,
     p_amount expense.amount%type,
     p_unit expense.unit%type,
     p_year expense.year%type,
     p_month expense.month%type,
     p_day expense.day%type,
     p_name expense.name%type,
     p_cat_name IN VARCHAR)
    IS
     v_cat_name exp_cat.e_cat_name%TYPE;
     v_cat_id exp_cat.e_cat_id%TYPE;
     
    test_val NUMBER;
    BEGIN
    
    SELECT COUNT(*) INTO test_val FROM exp_cat WHERE e_cat_name = p_cat_name AND rownum = 1;
    IF test_val = 1 THEN
        SELECT e_cat_id INTO v_cat_id FROM exp_cat WHERE e_cat_name = p_cat_name;
    ELSE
        INSERT INTO exp_cat
        (e_cat_id, e_cat_name)
        VALUES
        (exp_cat_id_seq.NEXTVAL, p_cat_name);
        COMMIT;
        SELECT e_cat_id INTO v_cat_id FROM exp_cat WHERE e_cat_name = p_cat_name;
    END IF;
    
    INSERT INTO expense
    (exp_id, value, amount, unit, year, month, day, name, e_cat_id)
    VALUES
    (exp_id_seq.NEXTVAL, p_value, p_amount, p_unit, p_year, p_month, p_day, p_name, v_cat_id);
END insert_exp;
/
REM *********************************************
CREATE OR REPLACE TRIGGER insert_exp_trig
AFTER INSERT ON expense
    FOR EACH ROW
    DECLARE
        val      INTEGER;
        val_mon  INTEGER;
        val_year INTEGER;
        test_val INTEGER;
        test_val2 INTEGER;
        prev_sum INTEGER;
    BEGIN
        --Get values of new insert
        val := :NEW.value;
        val_mon := :NEW.month;
        val_year := :NEW.year;
        --Check if the summary entry for given month exist
        SELECT COUNT(*) INTO test_val FROM summary WHERE val_mon = month AND val_year = year AND rownum = 1;
        IF test_val = 0 THEN
            SELECT COUNT(*) INTO test_val2 FROM summary WHERE (month < val_mon AND year = val_year) OR (year < val_year);
                IF test_val2 != 0 THEN
                    SELECT balance INTO prev_sum FROM
                    (SELECT * FROM summary WHERE (month < val_mon AND year = val_year) OR (year < val_year) ORDER BY year DESC, month DESC)
                    WHERE ROWNUM = 1;
                ELSE
                    prev_sum := 0;
                END IF;
            INSERT INTO summary(year, month, balance)
            VALUES(val_year, val_mon, prev_sum);
        END IF;
        UPDATE summary SET balance = balance-val WHERE (month >= val_mon AND year = val_year) OR year > val_year;
END;
/
REM *********************************************
CREATE OR REPLACE PROCEDURE update_rev
    (p_rev_id revenue.rev_id%type,
     p_value revenue.value%type,
     p_amount revenue.amount%type,
     p_unit revenue.unit%type,
     p_year revenue.year%type,
     p_month revenue.month%type,
     p_day revenue.day%type,
     p_name revenue.name%type,
     p_cat_name IN VARCHAR)
    IS
     v_cat_name rev_cat.r_cat_name%TYPE;
     v_cat_id rev_cat.r_cat_id%TYPE;
     test_cat VARCHAR(20);
     test_val NUMBER;
    BEGIN
    --Update all the values except category 
    UPDATE revenue SET 
        value=p_value,
        amount=p_amount,
        unit=p_unit,
        year=p_year,
        month=p_month,
        day=p_day,
        name=p_name WHERE rev_id = p_rev_id;
    --check current category
    SELECT r_cat_name INTO test_cat FROM rev_cat rc, revenue rev WHERE rev.rev_id = p_rev_id AND rev.r_cat_id = rc.r_cat_id;
    IF p_cat_name != test_cat THEN
        SELECT COUNT(*) INTO test_val FROM rev_cat WHERE r_cat_name = p_cat_name AND rownum = 1;
        IF test_val = 1 THEN
            SELECT r_cat_id INTO v_cat_id FROM rev_cat WHERE r_cat_name = p_cat_name;
        ELSE
            INSERT INTO rev_cat
            (r_cat_id, r_cat_name)
            VALUES
            (rev_cat_id_seq.NEXTVAL, p_cat_name);
            COMMIT;
            SELECT r_cat_id INTO v_cat_id FROM rev_cat WHERE r_cat_name = p_cat_name;
        END IF;
    END IF;
END update_rev;
/
REM *********************************************
CREATE OR REPLACE TRIGGER update_rev_trig
    BEFORE UPDATE ON revenue
    FOR EACH ROW
    BEGIN
        --modify balance value
        UPDATE summary SET balance = balance-:OLD.value WHERE (month >= :OLD.month AND year = :OLD.year) OR year > :OLD.year;
        DBMS_OUTPUT.PUT_LINE(:OLD.value);
        UPDATE summary SET balance = balance+:NEW.value WHERE (month >= :NEW.month AND year = :OLD.year) OR year > :NEW.year;
        DBMS_OUTPUT.PUT_LINE(:NEW.value);
END;
/
REM *********************************************
CREATE OR REPLACE PROCEDURE update_exp
    (p_exp_id expense.exp_id%type,
     p_value expense.value%type,
     p_amount expense.amount%type,
     p_unit expense.unit%type,
     p_year expense.year%type,
     p_month expense.month%type,
     p_day expense.day%type,
     p_name expense.name%type,
     p_cat_name IN VARCHAR)
    IS
     v_cat_name exp_cat.e_cat_name%TYPE;
     v_cat_id exp_cat.e_cat_id%TYPE;
     test_cat VARCHAR(20);
     test_val NUMBER;
    BEGIN
    --Update all the values except category 
    UPDATE expense SET 
        value=p_value,
        amount=p_amount,
        unit=p_unit,
        year=p_year,
        month=p_month,
        day=p_day,
        name=p_name WHERE exp_id = p_exp_id;
    --check current category
    SELECT e_cat_name INTO test_cat FROM exp_cat ec, expense exp WHERE exp.exp_id = p_exp_id AND exp.e_cat_id = ec.e_cat_id;
    IF p_cat_name != test_cat THEN
        SELECT COUNT(*) INTO test_val FROM exp_cat WHERE e_cat_name = p_cat_name AND rownum = 1;
        IF test_val = 1 THEN
            SELECT e_cat_id INTO v_cat_id FROM exp_cat WHERE e_cat_name = p_cat_name;
        ELSE
            INSERT INTO exp_cat
            (e_cat_id, e_cat_name)
            VALUES
            (exp_cat_id_seq.NEXTVAL, p_cat_name);
            COMMIT;
            SELECT e_cat_id INTO v_cat_id FROM exp_cat WHERE e_cat_name = p_cat_name;
        END IF;
    END IF;
    END update_exp;
/
REM *********************************************
CREATE OR REPLACE TRIGGER update_exp_trig
    BEFORE UPDATE ON expense
    FOR EACH ROW
    BEGIN
        --modify balance value
        UPDATE summary SET balance = balance+:OLD.value WHERE (month >= :OLD.month AND year = :OLD.year) OR year > :OLD.year;
        DBMS_OUTPUT.PUT_LINE(:OLD.value);
        UPDATE summary SET balance = balance-:NEW.value WHERE (month >= :NEW.month AND year = :OLD.year) OR year > :NEW.year;
        DBMS_OUTPUT.PUT_LINE(:NEW.value);
END;
/    
REM *********************************************
CREATE OR REPLACE PROCEDURE delete_rev
    (p_rev_id revenue.rev_id%type)
    IS
    BEGIN
    DELETE FROM revenue WHERE rev_id = p_rev_id;
END delete_rev;
/
REM *********************************************
CREATE OR REPLACE TRIGGER delete_rev_trig
    BEFORE DELETE ON revenue
    FOR EACH ROW
    BEGIN
        --modify balance value
        UPDATE summary SET balance = balance-:OLD.value WHERE (month >= :OLD.month AND year = :OLD.year) OR year > :OLD.year;
END;
/
REM *********************************************
CREATE OR REPLACE PROCEDURE delete_exp
    (p_exp_id expense.exp_id%type)
    IS
    BEGIN
    DELETE FROM expense WHERE exp_id = p_exp_id;
END delete_exp;
/
REM *********************************************
CREATE OR REPLACE TRIGGER delete_exp_trig
    BEFORE DELETE ON expense
    FOR EACH ROW
    BEGIN
        --modify balance value
        UPDATE summary SET balance = balance+:OLD.value WHERE (month >= :OLD.month AND year = :OLD.year) OR year > :OLD.year;
END;    
/
REM *********************************************
CREATE OR REPLACE PROCEDURE insert_new
IS
    val_mon  INTEGER;
    val_year INTEGER;
    test_val INTEGER;
    test_val2 INTEGER;
    prev_sum INTEGER;
BEGIN
    --Get values of new insert
    SELECT to_number(to_char(sysdate, 'YYYY')) INTO val_year from dual;
    SELECT to_number(to_char(sysdate, 'MM')) INTO val_mon from dual;
    --Check if the summary entry for given month exist
    SELECT COUNT(*) INTO test_val FROM summary WHERE val_mon = month AND val_year = year AND rownum = 1;
    IF test_val = 0 THEN
        SELECT COUNT(*) INTO test_val2 FROM summary WHERE (month < val_mon AND year = val_year) OR (year < val_year);
            IF test_val2 != 0 THEN
                SELECT balance INTO prev_sum FROM
                (SELECT * FROM summary WHERE (month < val_mon AND year = val_year) OR (year < val_year) ORDER BY year DESC, month DESC)
                WHERE ROWNUM = 1;
            ELSE
                prev_sum := 0;
            END IF;
        INSERT INTO summary(year, month, balance)
        VALUES(val_year, val_mon, prev_sum);
    END IF;   
END;
/
REM *********************************************
COMMIT;