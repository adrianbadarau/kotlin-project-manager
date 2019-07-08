export interface IField {
  id?: string;
  name?: string;
  data?: string;
}

export class Field implements IField {
  constructor(public id?: string, public name?: string, public data?: string) {}
}
