-- create extension vector;

drop table if exists article;

create table article (
  id serial primary key,  
  content text,
  embedding vector(384)
);