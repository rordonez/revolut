package rafael.ordonez.revolut.model.user;

import org.springframework.hateoas.Identifiable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by rafa on 27/9/15.
 */
@Entity
@Table(name = "users")
public class User implements Identifiable<Long>, Serializable{

    private static final long serialVersionUID = -3515872296701004938L;
    @Id
    private Long id;

    @Column(name = "name")
    private String name;

    @Override
    public Long getId() {
        return id;
    }
}
