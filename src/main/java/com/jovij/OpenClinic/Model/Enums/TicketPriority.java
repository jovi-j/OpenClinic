package com.jovij.OpenClinic.Model.Enums;

public enum TicketPriority {
    NORMAL{
        @Override
        public String toString() {
            return "NMT";
        }
    },
    EXAM_RESULTS{
        @Override
        public String toString() {
            return "EXT";
        }
    },
    PREFERENTIAL{
        @Override
        public String toString() {
            return "PRT";
        }
    };
}
