package proj.concert.service.services;

import proj.concert.common.dto.*;
import proj.concert.service.domain.*;

import java.time.LocalDateTime;
import java.util.*;

import proj.concert.service.mapper.*;
import org.slf4j.*;
import javax.ws.rs.CookieParam;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.*;
import javax.persistence.*;

//retrieving concert
@Path("/concert-service")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConcertResource {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConcertResource.class);
	@GET
	@Path("/concerts/{id}")
	public Response retrieveConcert(@PathParam("id") long id) {

		LOGGER.info("Retrieving concert with id: " + id);

		EntityManager em = PersistenceManager.instance().createEntityManager();

		try {
			em.getTransaction().begin();

			Concert concert = em.find(Concert.class, id);

			em.getTransaction().commit();

			if (concert == null)
				return Response.status(Response.Status.NOT_FOUND).build();
			ConcertDTO concertDTO = ConcertMapper.toDto(concert);

			return Response.ok(concertDTO).build();
		} finally {
			em.close();
		}
	}
	//retrieving list of concerts
	@GET
	@Path("/concerts")
	public Response retrieveAllConcerts() {
		LOGGER.info("Retrieving all concerts");
		EntityManager em = PersistenceManager.instance().createEntityManager();
		try {
			em.getTransaction().begin();

			TypedQuery<Concert> concertQuery = em.createQuery("select c from Concert c", Concert.class);
			List<Concert> concerts = concertQuery.getResultList();

			em.getTransaction().commit();

			if (concerts == null)
				return Response.status(Response.Status.NOT_FOUND).build();

			List<ConcertDTO> concertsDTO = new ArrayList<>();

			for (Concert concert : concerts)
				concertsDTO.add(ConcertMapper.toDto(concert));
			GenericEntity<List<ConcertDTO>> entity = new GenericEntity<>(concertsDTO) {
			};
			return Response.ok(entity).build();
		} finally {
			em.close();
		}
	}
	//concert summaries
	@GET
	@Path("/concerts/summaries")
	public Response getConcertSummaries() {
		LOGGER.info("Retrieving all concerts summaries");
		EntityManager em = PersistenceManager.instance().createEntityManager();
		try {
			em.getTransaction().begin();
			TypedQuery<Concert> concertQuery = em.createQuery("select c from Concert c", Concert.class);
			List<Concert> concerts = concertQuery.getResultList();
			em.getTransaction().commit();

			List<ConcertSummaryDTO> concertSummariesDTO = new ArrayList<>();
			for (Concert concert: concerts) {
				ConcertSummaryDTO concertSummaryDTO = new ConcertSummaryDTO(concert.getId(), concert.getTitle(), concert.getImageName());
				concertSummariesDTO.add(concertSummaryDTO);
			}
			GenericEntity<List<ConcertSummaryDTO>> entity = new GenericEntity<>(concertSummariesDTO) {
			};
			return Response.ok(entity).build();
		} finally {
			em.close();
		}
	}
	//Get performer by id
	@GET
	@Path("performers/{id}")
	public Response retrievePerformer(@PathParam("id") long id) {
		System.out.println("Called Successfully: retrievePerformer");
		LOGGER.info("Retrieving performer with id: " + id);
		EntityManager em = PersistenceManager.instance().createEntityManager();

		try {
			em.getTransaction().begin();

			Performer performer = em.find(Performer.class, id);

			em.getTransaction().commit();

			if (performer == null) {
				return Response.status(Response.Status.NOT_FOUND).build();
			} else {
				return Response.ok(PerformerMapper.toDto(performer)).build();
			}
		} finally {
			em.close();
		}
	}
	//get list of performers

	@GET
	@Path("/bookings")
	@Produces("application/java-serialization")
	public Response retrieveBookings(@CookieParam(Config.CLIENT_COOKIE) Cookie clientId) {
		LOGGER.info("Retrieving all bookings");
		EntityManager em = PersistenceManager.instance().createEntityManager();
		try {
			if(clientId != null) {
				em.getTransaction().begin();

				TypedQuery<Booking> bookingQuery = em.createQuery("select b from Booking b", Booking.class);
				List<Booking> bookings = bookingQuery.getResultList();
				em.getTransaction().commit();

				List<BookingDTO> BookingsDTO = new ArrayList<>();
				for (Booking booking : bookings)
					BookingsDTO.add(BookingMapper.toDto(booking));

				return Response.ok(BookingsDTO).build();
			} else {
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}

		} finally {
			em.close();
		}
	}

	@POST
	@Path("/bookings")
	@Consumes({
			MediaType.APPLICATION_JSON,
	})
	public Response makeBooking(BookingRequestDTO bookingDTO, @CookieParam(Config.CLIENT_COOKIE) Cookie clientId) {
		LOGGER.info("Making booking");
		EntityManager em = PersistenceManager.instance().createEntityManager();
		try {
			if(clientId != null) {
				em.getTransaction().begin();

				TypedQuery<Booking> bookingQuery = em.createQuery("select b from Booking b", Booking.class);
				List<Booking> bookings = bookingQuery.getResultList();
				System.out.println("length" + bookings.size());
				em.getTransaction().commit();

				for (Booking booking : bookings)
					if (bookingDTO.getConcertId() == booking.getId()
							&& bookingDTO.getDate() == booking.getDate()
					) {
						int i = 0;
						List<Seat> seats = booking.getSeats();
						for (Seat seat : seats) {
							if (seat.getLabel() == bookingDTO.getSeatLabels().get(i)
									&& seat.getIsBooked() == false
							) {
								seat.setIsBooked(true);
								i += 1;
							}
						}

						return Response.ok().build();
					}

				return Response.ok().build();
			} else {

				return Response.status(Response.Status.UNAUTHORIZED).build();
			}

		} finally {
			em.close();
		}
	}





	@GET
	@Path("/seats/{search}")
	///seats/2020-02-15T20:00:00?status=Booked"
	public Response retrieveSeats(@PathParam("search") String booking_string) {
		LOGGER.info("Retrieving seats under booking");
		int year = Integer.parseInt(booking_string.substring(0,4));
		int month = Integer.parseInt(booking_string.substring(5,7));
		int dayOfMonth = Integer.parseInt(booking_string.substring(8,10));
		int hour = Integer.parseInt(booking_string.substring(11,13));
		int minute = Integer.parseInt(booking_string.substring(14,16));
		int second = Integer.parseInt(booking_string.substring(17,19));
		EntityManager em = PersistenceManager.instance().createEntityManager();
		try {
			em.getTransaction().begin();

			TypedQuery<Booking> bookingQuery = em.createQuery("select b from Booking b", Booking.class);
			List<Booking> bookings = bookingQuery.getResultList();
			em.getTransaction().commit();

			if (bookings == null)
				return Response.status(Response.Status.NOT_FOUND).build();
			List<BookingDTO> BookingsDTO = new ArrayList<>();
			for (Booking booking : bookings)
				if (booking.getDate() == LocalDateTime.of(year, month, dayOfMonth, hour, minute, second)) {

					return Response.ok(booking.getSeats()).build();
				}
			return Response.status(Response.Status.NOT_FOUND).build();



		} finally {
			em.close();
		}
	}



//Login user

	@POST
	@Path("/login")
	public Response Login(UserDTO userDTO) {
		LOGGER.info("Logging in");
		EntityManager em = PersistenceManager.instance()
				.createEntityManager();
		try {
			em.getTransaction()
					.begin();
			User user = em.createQuery("select user from User user where user.username = :username", User.class)
					.setParameter("username", userDTO.getUsername()).getResultList().stream().findFirst()
					.orElse(null);

			if (user == null) {
				return Response.status(Response.Status.UNAUTHORIZED)
						.build();
			} else {
				if (user.getPassword().equals(userDTO.getPassword())) {
					String token = UUID.randomUUID().toString();
					user.setCookie(token);
					em.merge(user);
					em.getTransaction().commit();

					NewCookie newCookie = new NewCookie(Config.CLIENT_COOKIE, UUID.randomUUID().toString());

					return Response.ok(user).cookie(newCookie).build();
				} else {
					return Response.status(Response.Status.UNAUTHORIZED).build();
				}
			}
		} finally {
			em.close();
		}
	}

	//get list of performers
	@GET
	@Path("/performers")
	public Response retrieveAllPerformers() {
		LOGGER.info("Retrieving all performers");
		EntityManager em = PersistenceManager.instance().createEntityManager();
		try {
			em.getTransaction().begin();
			TypedQuery<Performer> performerQuery = em.createQuery("select p from Performer p", Performer.class);
			List<Performer> performers = performerQuery.getResultList();
			em.getTransaction().commit();

			if (performers == null)
				return Response.status(Response.Status.NOT_FOUND).build();
			List<PerformerDTO> performersDTO = new ArrayList<>();
			for (Performer performer : performers)
				performersDTO.add(PerformerMapper.toDto(performer));
			GenericEntity<List<PerformerDTO>> entity = new GenericEntity<>(performersDTO) {
			};
			return Response.ok(entity).build();

		} finally {
			em.close();
		}
	}

//	@POST
//	@Path("/subscribe/concertInfo")
//	@Consumes(MediaType.APPLICATION_JSON)
//	public Response killMePlease(){
//		return Response.ok().build();
//	}
	private final List<AsyncResponse> subs = new Vector<>();

	@GET
	@Path("/subscribe/concertInfo")
	@Produces(MediaType.APPLICATION_JSON)
	public void subscribeToMessage(@Suspended AsyncResponse sub) {
		subs.add(sub);
	}

	/**
	 * POSTs a message, which will be pushed back to all subscribers.
	 *
	 * @param message the message to POST.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postMessage(ConcertInfoSubscription message) {

		LOGGER.warn(message.toString());

		synchronized (subs) {
			for (AsyncResponse sub : subs) {
				sub.resume(message);
			}
			subs.clear();
		}

		return Response.ok().build();

	}

}

