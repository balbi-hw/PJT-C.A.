from django.shortcuts import render

from rest_framework import status
from rest_framework.response import Response
from rest_framework.decorators import api_view

from django.contrib.auth import login as auth_login

from accounts.serializers import LoginSerializer, SignUpSerializer



@api_view(['GET'])
def login_view(request):
    serializer = LoginSerializer(data=request.data)

    if serializer.is_valid(raise_exception=True):
        user = serializer.validated_data['user']

        auth_login(request, user)

        return Response(
            {
                'id': user.id,
                'username': user.username,
                'message': '로그인 성공',
            },
            status=status.HTTP_200_OK
        )
    
    return Response(
        serializer.errors,
        status=status.HTTP_400_BAD_REQUEST
    )


@api_view(['POST'])
def register(request):
    serializer = SignUpSerializer(data=request.data)

    if serializer.is_valid(raise_exception=True):
        user = serializer.save()
        return Response(
            {
                'id': user.id,
                'username': user.username,
            },
            status=status.HTTP_201_CREATED
        )
    
    return Response(
        serializer.errors,
        status=status.HTTP_400_BAD_REQUEST
    )







