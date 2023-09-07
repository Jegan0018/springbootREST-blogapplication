package com.jegan.blogapplication.service;

import com.jegan.blogapplication.dao.TagRepository;
import com.jegan.blogapplication.entity.Post;
import com.jegan.blogapplication.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TagServiceImpl implements TagService {

    private TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Set<Tag> findOrCreateTag(Set<Tag> tagInput) {
        Set<Tag> tags = new HashSet<>();
        Tag tag;
        for (Tag tagObject : tagInput) {
            String name = tagObject.getName();
            Optional<Tag> existingTag = tagRepository.findByName(name);
            if (existingTag.isPresent()) {
                tag = existingTag.get();
            } else {
                tag = new Tag();
                tag.setName(name);
                tagRepository.save(tag);
            }
            tags.add(tag);
        }
        return tags;
    }

    @Override
    public Page<Post> findPostsByTags(List<String> filterByTags, Pageable pageable) {
        return tagRepository.findPostsByTags(filterByTags, pageable);
    }
}
