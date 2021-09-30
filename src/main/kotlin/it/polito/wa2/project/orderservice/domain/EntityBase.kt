package it.polito.wa2.project.orderservice.domain


import org.springframework.data.util.ProxyUtils
import java.io.Serializable
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class EntityBase<T: Serializable> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private var id:T? = null

    fun getId(): T? = id

    override fun toString(): String {
        return "@Entity ${this.javaClass.name}(id=$id)"
    }

    override fun equals(other: Any?): Boolean {
        if( other == null ) return false
        if( other === this ) return false
        if( javaClass != ProxyUtils.getUserClass(other) ) return false

        other as EntityBase<*>
        return if(id==null) false
        else this.id == other.id

    }

    override fun hashCode(): Int {
        return 41
    }

}
