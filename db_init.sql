-- create extension vector;

drop table if exists article;

create table article (
  id serial primary key,  
  body text not null,
  embedding vector(384)
);