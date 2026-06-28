import SwiftUI
import shared

struct ContentView: View {

    private let converter = MeasurementConverter()
    private let unitsProvider = UnitsProvider()

    var body: some View {
        VStack(spacing: 16) {
            Text("SensorsManager")
                .font(.largeTitle)
                .bold()
            Text("Shared Kotlin Multiplatform module")
                .font(.subheadline)
                .foregroundColor(.secondary)

            Divider()

            Text("1 m = \(converter.convertDistance(distanceInMeters: 1.0, unit: DistanceUnit.feet)) ft")
            Text("1 rad = \(converter.convertAngle(angleInRadians: 1.0, unit: AngleUnit.degree)) °")
            Text("0 °C = \(converter.convertTemperature(temperatureInCelsius: 0.0, unit: TemperatureUnit.fahrenheit)) °F")

            Text("Supported distance units: \(supportedDistanceUnits)")
                .font(.footnote)
                .foregroundColor(.secondary)
        }
        .padding()
    }

    private var supportedDistanceUnits: String {
        let units = unitsProvider.getAvailableDistanceUnits()
        var names: [String] = []
        for index in 0..<units.size {
            if let unit = units.get(index: index) {
                names.append(String(describing: unit))
            }
        }
        return names.joined(separator: ", ")
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
