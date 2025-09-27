import { Component, signal, OnInit } from '@angular/core';
// import { RouterOutlet } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { NgFor, NgClass } from '@angular/common';
import axios from 'axios';

interface Order {
  id: string;
  orderStatusId: number;
  orderStatusName: string;
  orderDetail: string;
  price: number;
  customerName: string;
  updatedAt: string;
}



@Component({
  selector: 'app-root',
  imports: [FormsModule, NgFor, NgClass],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
  protected readonly title = signal('frontend');
  orders: Order[] = [];

  private mockFoods = [
    { name: 'Tom Yum Goong', price: 12.99 },
    { name: 'Pad Thai', price: 10.50 },
    { name: 'Green Curry', price: 13.75 },
    { name: 'Massaman Curry', price: 14.25 },
    { name: 'Som Tum', price: 8.99 },
    { name: 'Khao Pad', price: 9.50 },
    { name: 'Satay', price: 11.25 },
    { name: 'Mango Sticky Rice', price: 6.99 },
    { name: 'Spring Rolls', price: 7.50 },
    { name: 'Chicken Satay', price: 12.25 }
  ];

  name = 'frontend';
  surname = 'NG';

  ngOnInit() {
    this.fetchOrders();
  }

  async fetchOrders() {
    try {
      const response = await axios.get('http://localhost:8080/api/orders');
      // The API returns data in a nested structure
      this.orders = response.data.data.content;
    } catch (error) {
      console.error('Error fetching orders:', error);
    }
  }

  getStatusClass(status: string): string {
    switch (status.toLowerCase()) {
      case 'completed':
        return 'bg-success';
      case 'pending':
        return 'bg-warning';
      case 'processing':
        return 'bg-info';
      case 'cancelled':
        return 'bg-danger';
      default:
        return 'bg-secondary';
    }
  }

  showName() {
    return this.name + ' ' + this.surname;
  }
}