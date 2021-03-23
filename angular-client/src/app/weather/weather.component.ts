import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ForecastDataItemDto, WeatherService } from './weather.service';

@Component({
  selector: 'app-weather-component',
  templateUrl: './weather.component.html',
  styleUrls: ['./weather.component.scss'],
})
export class WeatherComponent implements OnInit {

  public form: FormGroup;
  public weatherData: ForecastDataItemDto[];
  public notFound: boolean;
  public badRequest: boolean;

  constructor(
    private formBuilder: FormBuilder,
    private weatherService: WeatherService,
  ) {
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      city: '',
    });
  }

  getWeather(formValues): void {
    this.weatherService.getWeather(formValues.city)
      .subscribe(
        forecastResponse => {
          this.weatherData = forecastResponse.forecast;
          this.badRequest = false;
          this.notFound = false;
        },
        error => {
          this.badRequest = error.status === 400;
          this.notFound = error.status === 404;
        },
      );
  }
}
