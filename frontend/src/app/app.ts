import { Component, signal, OnInit } from '@angular/core';
// import { RouterOutlet } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { NgFor, NgClass } from '@angular/common';
import axios from 'axios';
import Swal from 'sweetalert2';

interface Order {
  id: string;
  orderStatusId: number;
  orderStatusName: string;
  orderDetail: string;
  price: number;
  customerName: string;
  updatedAt: string;
}

interface Food {
  name: string;
  price: number;
}

interface OrderStatus {
  id: number,
  statusName: string;
}

@Component({
  selector: 'app-root',
  imports: [FormsModule, NgFor, NgClass],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
  protected readonly title = signal('frontend');
  private endpoint: string = `http://localhost:8080/api`;
  orders: Order[] = [];

  mockFoods: Food[] = [
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

  orderStatus: OrderStatus[] = [
    { id: 1, statusName: 'PENDING' },
    { id: 2, statusName: 'CONFIRMED' },
    { id: 3, statusName: 'COOKING' },
    { id: 4, statusName: 'DELIVERING' },
    { id: 5, statusName: 'COMPLETED' },
    { id: 6, statusName: 'CANCELLED' },
  ];

  name = 'Prasert';
  surname = 'Kulborekupt';

  ngOnInit() {
    this.fetchOrders();
  }

  async fetchOrders() {
    try {
      const response = await axios.get(`${this.endpoint}/orders`);
      // The API returns data in a nested structure
      this.orders = response.data.data.content;
    } catch (error) {
      console.error('Error fetching orders:', error);
    }
  }

  getStatusClass(id: number): string {

    switch (id) {
      case 1: return 'bg-secondary';
      case 2: return 'bg-primary';
      case 3: return 'bg-warning';
      case 4: return 'bg-info';
      case 5: return 'bg-success';
      case 6: return 'bg-danger';
      default: return 'bg-secondary';
    }
  }

  showName() {
    return this.name + ' ' + this.surname;
  }

  openCreateOrderModal() {
    const modalElement = document.getElementById('createOrderModal');
    if (modalElement) {
      const modal = new (window as any).bootstrap.Modal(modalElement);
      modal.show();
    }
  }

  async createOrder(index: number) {
    const selectedFood = this.mockFoods[index];
    const body = {
      orderStatusId: this.orderStatus[0].id,
      orderStatusName: this.orderStatus[0].statusName,
      orderDetail: selectedFood.name,
      price: selectedFood.price,
      customerName: this.showName()
    };


    const result = await Swal.fire({
      title: 'Confirm Order',
      text: `Are you sure you want to order ${selectedFood.name} for $${selectedFood.price}?`,
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Yes, create order',
      cancelButtonText: 'Cancel'
    });

    if (result.isConfirmed) {

      const { data } = await axios.post(`${this.endpoint}/orders`, body, {
        headers: {
          'Content-Type': 'application/json'
        }
      })

      console.log(data)

      if (data.status_code == 200) {
        Swal.fire({
          title: 'Order Created!',
          text: `Your order for ${selectedFood.name} has been created successfully.`,
          icon: 'success',
          confirmButtonText: 'OK'
        });

        this.orders.push(data.data)

      }

    }



  }
}