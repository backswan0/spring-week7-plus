package org.example.expert.common.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "todos")
public class Todo extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String contents;
    private String weather;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
        name = "user_id",
        nullable = false
    )
    private User user;

    @OneToMany(
        mappedBy = "todo",
        cascade = CascadeType.REMOVE
    )
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(
        mappedBy = "todo",
        cascade = CascadeType.PERSIST,
        orphanRemoval = true
    )
    private List<Manager> managers = new ArrayList<>();

    public Todo(
        String title,
        String contents,
        String weather,
        User user
    ) {
        this.title = title;
        this.contents = contents;
        this.weather = weather;
        this.user = user;
        this.managers.add(new Manager(user, this));
    }

    public Todo(
        String title) {
        this.title = title;
    }
}