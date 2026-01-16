package com.jovij.OpenClinic.Model.Enums;

public enum TicketStatus {
    WAITING_ATTENDANT{
        @Override
        public String toString() {
            return "WAITING ATTENDANT";
        }
    },
    WAITING_APPOINTMENT{
        @Override
        public String toString() {
            return "WAITING APPOINTMENT";
        }
    },
    UNREDEEMED{
        @Override
        public String toString() {
            return "UNREDEEMED";
        }
    },
    SERVED{
        @Override
        public String toString() {
            return "SERVED";
        }
    },
    CALLED_BY_ATTENDANT {
        @Override
        public String toString() {
            return "CALLED BY ATTENDANT";
        }
    },
    CALLED_BY_MEDIC {
        @Override
        public String toString() {
            return "CALLED BY MEDIC";
        }
    }
}
