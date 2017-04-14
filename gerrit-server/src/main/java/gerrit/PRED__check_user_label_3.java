begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|// you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|// You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|// distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|// See the License for the specific language governing permissions and
end_comment

begin_comment
comment|// limitations under the License.
end_comment

begin_package
DECL|package|gerrit
package|package
name|gerrit
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
operator|.
name|LabelType
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|restapi
operator|.
name|AuthException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|rules
operator|.
name|StoredValues
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|CurrentUser
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|permissions
operator|.
name|LabelPermission
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|permissions
operator|.
name|PermissionBackendException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|query
operator|.
name|change
operator|.
name|ChangeData
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|exceptions
operator|.
name|IllegalTypeException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|exceptions
operator|.
name|JavaException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|exceptions
operator|.
name|PInstantiationException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|exceptions
operator|.
name|PrologException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|exceptions
operator|.
name|SystemException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|IntegerTerm
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|JavaObjectTerm
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|Operation
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|Predicate
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|Prolog
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|SymbolTerm
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|Term
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|VariableTerm
import|;
end_import

begin_comment
comment|/**  * Checks user can set label to val.  *  *<pre>  *   '_check_user_label'(+Label, +CurrentUser, +Val)  *</pre>  */
end_comment

begin_class
DECL|class|PRED__check_user_label_3
class|class
name|PRED__check_user_label_3
extends|extends
name|Predicate
operator|.
name|P3
block|{
DECL|method|PRED__check_user_label_3 (Term a1, Term a2, Term a3, Operation n)
name|PRED__check_user_label_3
parameter_list|(
name|Term
name|a1
parameter_list|,
name|Term
name|a2
parameter_list|,
name|Term
name|a3
parameter_list|,
name|Operation
name|n
parameter_list|)
block|{
name|arg1
operator|=
name|a1
expr_stmt|;
name|arg2
operator|=
name|a2
expr_stmt|;
name|arg3
operator|=
name|a3
expr_stmt|;
name|cont
operator|=
name|n
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|exec (Prolog engine)
specifier|public
name|Operation
name|exec
parameter_list|(
name|Prolog
name|engine
parameter_list|)
throws|throws
name|PrologException
block|{
name|engine
operator|.
name|setB0
argument_list|()
expr_stmt|;
name|Term
name|a1
init|=
name|arg1
operator|.
name|dereference
argument_list|()
decl_stmt|;
name|Term
name|a2
init|=
name|arg2
operator|.
name|dereference
argument_list|()
decl_stmt|;
name|Term
name|a3
init|=
name|arg3
operator|.
name|dereference
argument_list|()
decl_stmt|;
if|if
condition|(
name|a1
operator|instanceof
name|VariableTerm
condition|)
block|{
throw|throw
operator|new
name|PInstantiationException
argument_list|(
name|this
argument_list|,
literal|1
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
operator|(
name|a1
operator|instanceof
name|SymbolTerm
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalTypeException
argument_list|(
name|this
argument_list|,
literal|1
argument_list|,
literal|"atom"
argument_list|,
name|a1
argument_list|)
throw|;
block|}
name|String
name|label
init|=
name|a1
operator|.
name|name
argument_list|()
decl_stmt|;
if|if
condition|(
name|a2
operator|instanceof
name|VariableTerm
condition|)
block|{
throw|throw
operator|new
name|PInstantiationException
argument_list|(
name|this
argument_list|,
literal|2
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
operator|(
name|a2
operator|instanceof
name|JavaObjectTerm
operator|)
operator|||
operator|!
name|a2
operator|.
name|convertible
argument_list|(
name|CurrentUser
operator|.
name|class
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalTypeException
argument_list|(
name|this
argument_list|,
literal|2
argument_list|,
literal|"CurrentUser)"
argument_list|,
name|a2
argument_list|)
throw|;
block|}
name|CurrentUser
name|user
init|=
call|(
name|CurrentUser
call|)
argument_list|(
operator|(
name|JavaObjectTerm
operator|)
name|a2
argument_list|)
operator|.
name|object
argument_list|()
decl_stmt|;
if|if
condition|(
name|a3
operator|instanceof
name|VariableTerm
condition|)
block|{
throw|throw
operator|new
name|PInstantiationException
argument_list|(
name|this
argument_list|,
literal|3
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
operator|(
name|a3
operator|instanceof
name|IntegerTerm
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalTypeException
argument_list|(
name|this
argument_list|,
literal|3
argument_list|,
literal|"integer"
argument_list|,
name|a3
argument_list|)
throw|;
block|}
name|short
name|val
init|=
call|(
name|short
call|)
argument_list|(
operator|(
name|IntegerTerm
operator|)
name|a3
argument_list|)
operator|.
name|intValue
argument_list|()
decl_stmt|;
try|try
block|{
name|ChangeData
name|cd
init|=
name|StoredValues
operator|.
name|CHANGE_DATA
operator|.
name|get
argument_list|(
name|engine
argument_list|)
decl_stmt|;
name|LabelType
name|type
init|=
name|cd
operator|.
name|getLabelTypes
argument_list|()
operator|.
name|byLabel
argument_list|(
name|label
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
return|return
name|engine
operator|.
name|fail
argument_list|()
return|;
block|}
name|StoredValues
operator|.
name|PERMISSION_BACKEND
operator|.
name|get
argument_list|(
name|engine
argument_list|)
operator|.
name|user
argument_list|(
name|user
argument_list|)
operator|.
name|change
argument_list|(
name|cd
argument_list|)
operator|.
name|check
argument_list|(
operator|new
name|LabelPermission
operator|.
name|WithValue
argument_list|(
name|type
argument_list|,
name|val
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|cont
return|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|err
parameter_list|)
block|{
throw|throw
operator|new
name|JavaException
argument_list|(
name|this
argument_list|,
literal|1
argument_list|,
name|err
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|AuthException
name|err
parameter_list|)
block|{
return|return
name|engine
operator|.
name|fail
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|PermissionBackendException
name|err
parameter_list|)
block|{
name|SystemException
name|se
init|=
operator|new
name|SystemException
argument_list|(
name|err
operator|.
name|getMessage
argument_list|()
argument_list|)
decl_stmt|;
name|se
operator|.
name|initCause
argument_list|(
name|err
argument_list|)
expr_stmt|;
throw|throw
name|se
throw|;
block|}
block|}
block|}
end_class

end_unit

