import { Component, signal, OnInit } from '@angular/core';
// import { RouterOutlet } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { NgFor, NgClass, NgIf } from '@angular/common';
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
  imports: [FormsModule, NgFor, NgClass, NgIf],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
  protected readonly title = signal('Frontend');
  private endpoint: string = `http://localhost:8080/api`;
  orders: Order[] = [];
  selectedOrder: Order | null = null;
  availableStatuses: OrderStatus[] = [];

  totalPrice = 0;

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

      for (let i = 0; i < this.orders.length; i++) {
        if (this.orders[i].orderStatusId != 6) {
          this.totalPrice += this.orders[i].price;
        }
      }
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

  formatDateTime(dateString: string): string {
    if (!dateString) return 'N/A';

    const date = new Date(dateString);

    // 1. Check if the date object is valid after parsing
    if (isNaN(date.getTime())) {
      // Handle invalid date strings (e.g., "invalid date", "abc")
      return 'Invalid Date';
    }

    // 2. Use 'en-GB' locale but specify 'timeZone' as 'UTC' 
    // to ensure consistent output, otherwise the output will vary based on the user's timezone.
    // We remove the .replace(',', '') because the 'en-GB' format with both date and time 
    // components typically includes a comma between them, which is correct formatting.
    // The original code was removing a valid separator.
    return new Intl.DateTimeFormat('en-GB', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit',
      hour12: false,
      timeZone: 'UTC', // Ensures the date and time are displayed in UTC, not local time
    }).format(date);
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
  html: `Are you sure you want to order <br> [${selectedFood.name}]
         for [${selectedFood.price}] ?`,
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



      if (data.status_code == 200) {
        Swal.fire({
          title: 'Order Created!',
          html: `Your order for [${selectedFood.name}] <br> has been created successfully.`,
          icon: 'success',
          confirmButtonText: 'OK'
        });

        this.orders.push(data.data)
        this.totalPrice += selectedFood.price
      }
    }
  }

  getTotalPrice(): number {
    return Number(this.totalPrice.toFixed(2));
  }

  getAvailableStatuses(currentStatusId: number): OrderStatus[] {
    if (currentStatusId === 6 || currentStatusId === 5) {
      return [];
    }

    const available: OrderStatus[] = [];

    // Add next status (current + 1)
    const nextStatus = this.orderStatus.find(status => status.id === currentStatusId + 1);
    if (nextStatus) {
      available.push(nextStatus);
    }

    // Special case for status 1: also show status 6 (CANCELLED)
    if (currentStatusId != 5) {
      const cancelStatus = this.orderStatus.find(status => status.id === 6);
      if (cancelStatus) {
        available.push(cancelStatus);
      }
    }

    return available;
  }

  // Method to open the status popup
  openStatusPopup(order: Order) {
    this.selectedOrder = order;
    this.availableStatuses = this.getAvailableStatuses(order.orderStatusId);

    // Show the Bootstrap modal
    const modalElement = document.getElementById('statusPopup');
    if (modalElement) {
      const modal = new (window as any).bootstrap.Modal(modalElement);
      modal.show();
    }
  }

  // Method to update order status
  async updateOrderStatus(newStatus: OrderStatus) {

    if (this.selectedOrder) {

      const body = {
        "statusId": newStatus.id,
        "statusName": newStatus.statusName
      }

      console.log(body)
      console.log(newStatus.id)


      console.log(body, this.selectedOrder.id)
      const { data } = await axios.put(`${this.endpoint}/orders/${this.selectedOrder.id}`, body, {
        headers: {
          'Content-Type': 'application/json'
        }
      });

      if (data.status_code == 200) {

        // Show confirmation
        await Swal.fire({
          title: 'Status Updated!',
          html: `Order status has been updated to [${newStatus.statusName}].`,
          icon: 'success',
          confirmButtonText: 'OK'
        });
        console.log(`Updating order [${this.selectedOrder.id}] to status [${newStatus.statusName}]`);

        //Update UI
        this.selectedOrder.orderStatusId = newStatus.id;
        this.selectedOrder.orderStatusName = newStatus.statusName;
        this.selectedOrder.updatedAt = data.data.updatedAt;

      } else {
        // Show confirmation
        Swal.fire({
          title: 'Status Updated!',
          text: `Fail updated to [${newStatus.statusName}].`,
          icon: 'warning',
          confirmButtonText: 'Something went wrong.'
        });
      }

    }


  }

}