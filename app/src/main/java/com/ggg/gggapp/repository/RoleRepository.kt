package com.ggg.gggapp.repository

import com.ggg.gggapp.model.Role
import com.ggg.gggapp.remote_model.MessageResponse
import com.ggg.gggapp.remote_model.OneParamRequest
import com.ggg.gggapp.remote_model.RolenameAndPermissionRequest
import com.ggg.gggapp.remote_model.UpdateRoleRequest
import com.ggg.gggapp.service.RoleService
import com.ggg.gggapp.utils.generateToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Named

class RoleRepository @Inject constructor(@Named("roleService") private val service: RoleService) {
    fun getRoles(token: String): Flow<ArrayList<Role>> {
        return flow {
            emit(service.getRoles(generateToken(token)))
        }
    }

    fun findRoleByName(token: String, roleName: String): Flow<Role> {
        return flow {
            emit(service.findRoleByName(generateToken(token), OneParamRequest(roleName)))
        }
    }


    fun editRole(
        token: String,
        id: Long,
        roleName: String,
        permission: String
    ): Flow<MessageResponse> {
        return flow {
            emit(
                service.editRoleName(
                    generateToken(token),
                    UpdateRoleRequest(id, roleName, permission)
                )
            )
        }
    }

    fun addRole(token: String, roleName: String, permission: String): Flow<MessageResponse> {
        return flow {
            emit(
                service.addRole(
                    generateToken(token),
                    RolenameAndPermissionRequest(roleName, permission)
                )
            )
        }
    }

}