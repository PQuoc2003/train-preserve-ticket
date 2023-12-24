package com.hyperion.train_preserve_ticket.interfaces;

import com.hyperion.train_preserve_ticket.model.Trips;
import com.hyperion.train_preserve_ticket.model.Users;


public interface UserSearchRVInterface {

    /**
     * Get the trip in tripsDAO Arraylist by position, then add to trip preserve by id from firebaseAuth
     * and trip from tripsDAO list.
     * @param position position of trips which need to add to user preserve
     */
    void addUserToTrip(int position);

    void cancelToTrip(int position);


}
