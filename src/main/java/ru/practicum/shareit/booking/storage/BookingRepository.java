package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
            select b
            from Booking as b
            join b.booker as u
            join b.item as i
            join i.owner as o
            where b.id = :bookingId
            and (u.id = :userId or o.id = :userId)""")
    Optional<Booking> getByUserIdOrOwnerItem(Long bookingId, Long userId);

    List<Booking> findAllByBooker_id(Long bookerId);

    List<Booking> findAllByBooker_idAndEndBefore(Long bookerId, LocalDateTime currentDate);

    List<Booking> findAllByBooker_idAndStartAfter(Long bookerId, LocalDateTime currentDate);

    @Query("""
            select b
            from Booking as b
            join b.booker as u
            where u.id = :bookerId
            and :currentDate between b.start and b.end
            """)
    List<Booking> findAllByBookerAndNowBetweenStartAndEnd(Long bookerId, LocalDateTime currentDate);

    List<Booking> findAllByBooker_idAndStatus(Long bookerId, BookingStatus status);

    List<Booking> findAllByItem_Owner_id(Long ownerId);

    @Query("""
            select b
            from Booking as b
            join b.item as i
            join i.owner as o
            where o.id = :ownerId
            and :currentDate between b.start and b.end
            """)
    List<Booking> findAllByOwnerAndNowBetweenStartAndEnd(Long ownerId, LocalDateTime currentDate);

    List<Booking> findAllByItem_Owner_idAndEndBefore(Long ownerId, LocalDateTime currentDate);

    List<Booking> findAllByItem_Owner_idAndStartAfter(Long ownerId, LocalDateTime currentDate);

    List<Booking> findAllByItem_Owner_idAndStatus(Long ownerId, BookingStatus status);

    Optional<Booking> findFirstByItem_Id_AndBooker_idAndEndBeforeAndStatus(Long itemId, Long bookerId,
                                                                           LocalDateTime currentDate,
                                                                           BookingStatus status);
    Optional<Booking> findFirstByItem_IdAndStatusAndStartBeforeAndEndAfterOrderByEndDesc(Long itemId,
                                                                                         BookingStatus status,
                                                                                         LocalDateTime now,
                                                                                         LocalDateTime currentDate);

    Optional<Booking> findFirstByItem_IdAndStartAfterOrderByStartAsc(Long itemId, LocalDateTime currentDate);
}
