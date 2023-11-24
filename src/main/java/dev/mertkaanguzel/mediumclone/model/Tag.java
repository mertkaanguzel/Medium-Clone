package dev.mertkaanguzel.mediumclone.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

//@Table(name = "Tag")
@Entity
public class Tag {
    /*
    @Id
    @GeneratedValue
    private Long id;

     */
    @Id
    //@Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade =  {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "tags"
    )
    @JsonIgnore
    private Set<Article> articles = new HashSet<>();
    public Tag(String name) {
        //this.id = null;
        this.name = name;
    }

    protected Tag() {
    }
/*
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
*/
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Article> getArticles() {
        return articles;
    }

    public void setArticles(Set<Article> articles) {
        this.articles = articles;
    }
}
