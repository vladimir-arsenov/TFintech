package org.example.configuration.commands;

import lombok.RequiredArgsConstructor;
import org.example.client.ApiClient;
import org.example.service.CategoryService;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class InitializeCategoriesCommand implements Command {
    private final ApiClient apiClient;
    private final CategoryService categoryService;

    @Override
    public void execute() {
        Arrays.stream(apiClient.getCategories())
              .forEach(categoryService::addCategory);
    }
}