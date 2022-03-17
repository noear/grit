
-- // 2022-03-17

ALTER TABLE `grit_resource` ADD COLUMN `guid` varchar(40) NULL AFTER `attrs`;

UPDATE `grit_resource` SET `guid`= UUID() WHERE `guid` IS NULL;

ALTER TABLE `grit_resource` ADD UNIQUE INDEX `IX_grit_resource__guid`(`guid`) USING BTREE;


ALTER TABLE `grit_subject` ADD COLUMN `guid` varchar(40) NULL AFTER `attrs`;

UPDATE `grit_subject` SET `guid`= UUID() WHERE `guid` IS NULL;

ALTER TABLE `grit_subject` ADD UNIQUE INDEX `IX_grit_subject__guid`(`guid`) USING BTREE;