export interface IChangeHistory {
  id?: string;
  subject?: string;
  fromValue?: string;
  toValue?: string;
}

export class ChangeHistory implements IChangeHistory {
  constructor(public id?: string, public subject?: string, public fromValue?: string, public toValue?: string) {}
}
