export interface IBenefitType {
  id?: string;
  name?: string;
}

export class BenefitType implements IBenefitType {
  constructor(public id?: string, public name?: string) {}
}
