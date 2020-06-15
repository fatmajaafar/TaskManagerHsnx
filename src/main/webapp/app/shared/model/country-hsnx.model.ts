export interface ICountryHsnx {
  id?: number;
  countryName?: string;
  countryCode?: string;
}

export class CountryHsnx implements ICountryHsnx {
  constructor(public id?: number, public countryName?: string, public countryCode?: string) {}
}
