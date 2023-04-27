-- create users
CREATE TABLE IF NOT EXISTS `s12_ManageTheDiscord`.`users`
(
    `id`
    INT
    NOT
    NULL
    AUTO_INCREMENT,
    `username`
    VARCHAR
(
    50
) NOT NULL,
    `discord_id` VARCHAR
(
    50
) NOT NULL,
    `role` VARCHAR
(
    50
) NOT NULL,
    `level` VARCHAR
(
    50
) NOT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY
(
    `id`
)
    ) ENGINE = InnoDB;

-- ##new_query
-- users getAllUsers
CREATE PROCEDURE IF NOT EXISTS getAllUsers() BEGIN SELECT * FROM users;
END

-- ##new_query
-- users getUser
CREATE PROCEDURE IF NOT EXISTS getUser(IN id INT) BEGIN SELECT * FROM users WHERE users.id=id;
END

-- ##new_query
-- users addUser
CREATE PROCEDURE IF NOT EXISTS addUser(IN username VARCHAR(50), IN id VARCHAR(50), IN role VARCHAR(50), IN level VARCHAR(50)) BEGIN INSERT INTO `users` (`username`, `discord_id`, `role`, `level`) VALUES (username, id, role, level);
CALL getUser(id);
END

-- ##new_query
-- users updateUser
CREATE PROCEDURE IF NOT EXISTS updateUser(IN id INT, IN username VARCHAR(50), IN user_id VARCHAR(50), IN role VARCHAR(50), IN level VARCHAR(50))
BEGIN
UPDATE users
SET username = username,
    id       = user_id,
    role     = role,
    level    = level
WHERE id = user_id;
CALL getUser(id);
END


-- ##new_query
-- users deleteUser
CREATE PROCEDURE IF NOT EXISTS deleteUser(IN id INT) BEGIN DELETE FROM users WHERE users.id=id;
END

-- ##new_query
-- create roles
CREATE TABLE IF NOT EXISTS `s12_ManageTheDiscord`.`roles` (
    `id` VARCHAR (255) NOT NULL,
    `name` VARCHAR (50) NOT NULL,
    `isStaff` BOOLEAN NOT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB;


-- ##new_query
-- roles getAllRoles
CREATE PROCEDURE IF NOT EXISTS getAllRoles() BEGIN SELECT * FROM roles;
END

-- ##new_query
-- roles getRole
CREATE PROCEDURE IF NOT EXISTS getRole(IN id INT) BEGIN SELECT * FROM roles WHERE roles.id=id;
END

-- ##new_query
-- roles addRole
CREATE PROCEDURE IF NOT EXISTS addRole(IN id INT,IN name VARCHAR(50)) BEGIN INSERT INTO `roles` (`id`, `name`) VALUES (id, name);
CALL getRole(id);
END

-- ##new_query
-- roles updateRole
CREATE PROCEDURE IF NOT EXISTS updateRole(IN id INT, IN name VARCHAR(50)) BEGIN UPDATE roles SET name = name WHERE id = id;
CALL getRole(id);
END

-- ##new_query
-- roles deleteRole
CREATE PROCEDURE IF NOT EXISTS deleteRole(IN id INT) BEGIN DELETE FROM roles WHERE roles.id=id;
END

-- ##new_query
-- roles isStaff
CREATE PROCEDURE IF NOT EXISTS isStaff(IN id INT) BEGIN SELECT isStaff FROM roles WHERE roles.id=id;
END



