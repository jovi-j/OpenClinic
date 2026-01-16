package com.jovij.OpenClinic.Model.Enums;

public enum AppointmentStatus {
    OPEN{
        @Override
        public String toString() {
            return "OPEN";
        }
    },
    SCHEDULED{
        @Override
        public String toString() {
            return "SCHEDULED";
        }
    },
    ATTENDED {
        @Override
        public String toString() {
            return "ATTENDED";
        }
    },
    CANCELLED{
        @Override
        public String toString() {
            return "CANCELLED";
        }
    },
    ABSENT_PATIENT{
        @Override
        public String toString() {
            return "ABSENT PATIENT";
        }
    }
}
