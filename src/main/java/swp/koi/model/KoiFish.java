package swp.koi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import swp.koi.model.enums.KoiFishStatusEnum;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "KoiFish")
@AllArgsConstructor
public class KoiFish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer fishId;

    @Column(nullable = false)
    String gender;

    @Column(nullable = false)
    int age;

    @Column(nullable = false)
    float size;

    @Column(nullable = false)
    float price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    KoiFishStatusEnum status;

    @OneToOne(mappedBy = "koiFish", cascade = CascadeType.ALL)
    AuctionRequest auctionRequest;

    @OneToOne()
    @JoinColumn(name = "mediaId")
    Media media;

    @ManyToOne
    @JoinColumn(name = "varietyId", nullable = false)
    Variety variety;

    @OneToMany(mappedBy = "koiFish")
    List<Lot> lot;

    @OneToMany(mappedBy = "koiFish")
    List<Invoice> invoices;

    @ManyToOne
    @JoinColumn(name = "auctionTypeId")
    AuctionType auctionType;

    @OneToMany(mappedBy = "koiFish")
    List<Invoice> invoice;

    public KoiFish() {
    }

    public KoiFish(String gender, int age, float size, float price, KoiFishStatusEnum status, AuctionRequest auctionRequest, Media media, Variety variety) {
        this.gender = gender;
        this.age = age;
        this.size = size;
        this.price = price;
        this.status = status;
        this.auctionRequest = auctionRequest;
        this.media = media;
        this.variety = variety;
    }

}