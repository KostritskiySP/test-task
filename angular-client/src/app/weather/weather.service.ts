import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';


export interface ForecastResponse {
  forecast: [];
}

export interface ForecastDataItemDto {
  name: Date;
  value: number;
}

@Injectable({
  providedIn: 'root',
})
export class WeatherService {

  constructor(
    private http: HttpClient,
  ) {
  }

  getWeather(city: string): Observable<ForecastResponse> {
    const params = new HttpParams().set('city', city);
    return this.http.get<ForecastResponse>('http://localhost:8080/api/weather', { params });
  }
}
