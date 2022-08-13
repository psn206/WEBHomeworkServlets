package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {
    private PostController controller;
    private static final String API_PATH = "/api/posts";
    private static final String API_PATH_ID = "/api/posts/\\d+";

    @Override
    public void init() {
        final var repository = new PostRepository();
        final var service = new PostService(repository);
        controller = new PostController(service);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            final String path = req.getRequestURI();
            if (path.equals(API_PATH)) {
                controller.all(resp);
                return;
            }
            if (path.matches(API_PATH_ID)) {
                final long id = Long.parseLong(path.substring(path.lastIndexOf("/")));
                controller.getById(id, resp);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            final String path = req.getRequestURI();
            if (path.equals(API_PATH)) {
                controller.save(req.getReader(), resp);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            final String path = req.getRequestURI();
            if (path.matches(API_PATH_ID)) {
                final long id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
                controller.removeById(id, resp);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}