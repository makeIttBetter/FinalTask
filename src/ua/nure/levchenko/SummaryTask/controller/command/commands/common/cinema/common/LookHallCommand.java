package ua.nure.levchenko.SummaryTask.controller.command.commands.common.cinema.common;

import org.apache.log4j.Logger;
import ua.nure.levchenko.SummaryTask.controller.command.commands.Command;
import ua.nure.levchenko.SummaryTask.controller.command.consts.Actions;
import ua.nure.levchenko.SummaryTask.controller.command.consts.Attributes;
import ua.nure.levchenko.SummaryTask.controller.command.consts.Parameters;
import ua.nure.levchenko.SummaryTask.controller.jspPath.Path;
import ua.nure.levchenko.SummaryTask.exception.AppException;
import ua.nure.levchenko.SummaryTask.model.entity.db.Hall;
import ua.nure.levchenko.SummaryTask.model.entity.db.Reservation;
import ua.nure.levchenko.SummaryTask.model.entity.db.ScheduleEntity;
import ua.nure.levchenko.SummaryTask.model.services.ReservationService;
import ua.nure.levchenko.SummaryTask.model.services.ScheduleService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Displayed the page
 * with the cinema hall free and booked places
 * on the current session film watching
 *
 * @author K.Levchenko
 */
public class LookHallCommand implements Command {
    private static final Logger LOG = Logger.getLogger(LookHallCommand.class);

    /**
     * Command setting needed arguments to the needed scopes
     * before going to the page. Actually this command
     * then will redirect you to the page where free and booked
     * places of some film will be displayed
     *
     * @param request
     * @param response
     * @return
     * @throws AppException if some unexpected error occurred
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws AppException {
        LOG.debug("Command starts");

        // getting session
        HttpSession session = request.getSession();

        String action = request.getParameter(Parameters.ACTION);
        LOG.trace("Parameter required: action -->" + action);

        // setting attribute filmsSchedule
        session.setAttribute(Attributes.FILM_SCHEDULES, null);
        // setting attribute filmsSchedule
        session.setAttribute(Attributes.FILM_SCHEDULE_FREE_PLACES, null);

        String forward = Path.PAGE_CINEMA;
        ReservationService reservationService = new ReservationService();
        ScheduleService scheduleService = new ScheduleService();
        if (action.equals(Actions.GET_FILM)) {
            // getting filmId parameter from the request
            int scheduleId = Integer.parseInt(request.getParameter(Parameters.SCHEDULE_ID));
            LOG.trace("Request parameter: scheduleId --> " + scheduleId);

            // getting schedules from DB
            List<Reservation> hallReservedPlaces = reservationService.getAllFullBySchedule(scheduleId);
            ScheduleEntity currentScheduleEntity = scheduleService.readFull(scheduleId);
            Hall currentHall = currentScheduleEntity.getHall();
            LOG.trace("Request parameter set: filmScheduleEntity --> "
                    + hallReservedPlaces.toString());

            // preparing list of reservedPlaces number
            List<Integer> reservedPlaces = new ArrayList<>();
            for (Reservation reservation : hallReservedPlaces) {
                reservedPlaces.add(reservation.getPlaceNumber());
            }
            // preparing map of free/booked currentHallPlaces on current film in such hall
            Map<Integer, Boolean> currentHallPlaces = new LinkedHashMap<>();
            int placesAmount = currentHall.getPlacesAmount();
            for (int i = 1; i <= placesAmount; i++) {
                if (reservedPlaces.contains(i)) {
                    currentHallPlaces.put(i, false);
                } else {
                    currentHallPlaces.put(i, true);
                }
            }
            // setting attribute currentScheduleEntity
            session.setAttribute(Attributes.CURRENT_SCHEDULE_ENTITY, currentScheduleEntity);
            // setting attribute currentHallPlaces
            session.setAttribute(Attributes.PLACES, currentHallPlaces);
            // setting attribute placesToOrder
            List<Integer> placesToOrder = new ArrayList<>();
            session.setAttribute(Attributes.PLACES_TO_ORDER, placesToOrder);
        }
        LOG.debug("Command ends");
        return forward;
    }
}
