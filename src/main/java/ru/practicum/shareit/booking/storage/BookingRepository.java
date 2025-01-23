package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    List<Booking> findAllByBookerId(Long bookerId);

    List<Booking> findAllByBookerIdAndEndBefore(Long bookerId, LocalDateTime currentDate);

    List<Booking> findAllByBookerIdAndStartAfter(Long bookerId, LocalDateTime currentDate);

    @Query("""
            select b
            from Booking as b
            join b.booker as u
            where u.id = :bookerId
            and :currentDate between b.start and b.end
            """)
    List<Booking> findAllByBookerAndNowBetweenStartAndEnd(Long bookerId, LocalDateTime currentDate);

    List<Booking> findAllByBookerIdAndStatus(Long bookerId, BookingStatus status);

    List<Booking> findAllByItemOwnerId(Long ownerId);

    @Query("""
            select b
            from Booking as b
            join b.item as i
            join i.owner as o
            where o.id = :ownerId
            and :currentDate between b.start and b.end
            """)
    List<Booking> findAllByOwnerAndNowBetweenStartAndEnd(Long ownerId, LocalDateTime currentDate);

    List<Booking> findAllByItemOwnerIdAndEndBefore(Long ownerId, LocalDateTime currentDate);

    List<Booking> findAllByItemOwnerIdAndStartAfter(Long ownerId, LocalDateTime currentDate);

    List<Booking> findAllByItemOwnerIdAndStatus(Long ownerId, BookingStatus status);

    Optional<Booking> findFirstByItemIdAndBookerIdAndEndBeforeAndStatus(Long itemId, Long bookerId,
                                                                        LocalDateTime currentDate,
                                                                        BookingStatus status);

    Optional<Booking> findFirstByItemIdAndStatusAndStartBeforeAndEndAfterOrderByEndDesc(Long itemId,
                                                                                        BookingStatus status,
                                                                                        LocalDateTime now,
                                                                                        LocalDateTime currentDate);

    Optional<Booking> findFirstByItemIdAndStartAfterOrderByStartAsc(Long itemId, LocalDateTime currentDate);
}
