CREATE TABLE article (
  id char(64) NOT NULL,
  url varchar(2048) NOT NULL DEFAULT '',
  headline varchar(255) NOT NULL DEFAULT '',
  date int(10) NOT NULL,
  content text NOT NULL,
  summarisation text NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='utf8_general_ci'
