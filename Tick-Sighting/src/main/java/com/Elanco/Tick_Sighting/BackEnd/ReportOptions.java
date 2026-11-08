package com.Elanco.Tick_Sighting.BackEnd;

import java.util.List;

public record ReportOptions(List<String> species, List<String> locations) {
}
