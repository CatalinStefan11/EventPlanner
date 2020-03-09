package ro.ase.eventplanner.Util;

import java.util.List;

import ro.ase.eventplanner.Model.ServiceProvided;

public interface Callbacks {
    void onGetServices(List<ServiceProvided> serviceProvideds);
}
