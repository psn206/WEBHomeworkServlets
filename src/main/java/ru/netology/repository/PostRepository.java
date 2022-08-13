package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class PostRepository {

  private List<Post> postList = new CopyOnWriteArrayList<>();
  private AtomicInteger index = new AtomicInteger(0);

  public List<Post> all() {
    return postList;
  }

  public Optional<Post> getById(long id) {
    return postList.stream()
            .filter(o -> o.getId() == id)
            .findFirst();
  }

  public Post save(Post post) {
    if (post.getId() == 0) {
      post.setId(index.incrementAndGet());
      postList.add(post);
      return post;
    } else {
      for (int i = 0; i < postList.size(); i++) {
        Post currentPost = postList.get(i);
        if (currentPost.getId() == post.getId()) {
          currentPost.setContent(post.getContent());
          return currentPost;
        }
      }
      throw new NotFoundException();
    }
  }

  public void removeById(long id) {
    int index = -1;
    for (int i = 0; i < postList.size(); i++) {
      if (postList.get(i).getId() == id) index = i;
    }
    if (index != -1) postList.remove(index);
  }
}