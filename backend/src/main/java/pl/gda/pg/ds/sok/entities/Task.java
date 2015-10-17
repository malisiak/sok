package pl.gda.pg.ds.sok.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "tasks")
public class Task implements Serializable  {
	
	private static final long serialVersionUID = 6609599687003270487L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull
	@Size(max = 128)
	private String title;
	@NotNull
	@Size(max = 64)
	private String type;
	@NotNull
	@Type(type="text")
	private String content;
    @NotNull
    @Type(type="integer")
    private Integer difficulty;
	@OneToMany(mappedBy="task",cascade=CascadeType.PERSIST)
    private List<Answer> answers = new ArrayList<Answer>();
	
	public Task() {
	}
	
	public Task(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getType() {
		return type;
	}
	
	public String getContent() {
		return content;
	}

    public Integer getDifficulty() {
        return difficulty;
    }
}
