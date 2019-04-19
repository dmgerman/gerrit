begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|project
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
name|extensions
operator|.
name|restapi
operator|.
name|ResourceConflictException
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
name|reviewdb
operator|.
name|client
operator|.
name|Project
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
name|IdentifiedUser
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
name|git
operator|.
name|validators
operator|.
name|RefOperationValidators
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
name|validators
operator|.
name|ValidationException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|assistedinject
operator|.
name|Assisted
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|RefUpdate
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|transport
operator|.
name|ReceiveCommand
operator|.
name|Type
import|;
end_import

begin_class
DECL|class|RefValidationHelper
specifier|public
class|class
name|RefValidationHelper
block|{
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (Type operationType)
name|RefValidationHelper
name|create
parameter_list|(
name|Type
name|operationType
parameter_list|)
function_decl|;
block|}
DECL|field|refValidatorsFactory
specifier|private
specifier|final
name|RefOperationValidators
operator|.
name|Factory
name|refValidatorsFactory
decl_stmt|;
DECL|field|operationType
specifier|private
specifier|final
name|Type
name|operationType
decl_stmt|;
annotation|@
name|Inject
DECL|method|RefValidationHelper ( RefOperationValidators.Factory refValidatorsFactory, @Assisted Type operationType)
name|RefValidationHelper
parameter_list|(
name|RefOperationValidators
operator|.
name|Factory
name|refValidatorsFactory
parameter_list|,
annotation|@
name|Assisted
name|Type
name|operationType
parameter_list|)
block|{
name|this
operator|.
name|refValidatorsFactory
operator|=
name|refValidatorsFactory
expr_stmt|;
name|this
operator|.
name|operationType
operator|=
name|operationType
expr_stmt|;
block|}
DECL|method|validateRefOperation (String projectName, IdentifiedUser user, RefUpdate update)
specifier|public
name|void
name|validateRefOperation
parameter_list|(
name|String
name|projectName
parameter_list|,
name|IdentifiedUser
name|user
parameter_list|,
name|RefUpdate
name|update
parameter_list|)
throws|throws
name|ResourceConflictException
block|{
name|RefOperationValidators
name|refValidators
init|=
name|refValidatorsFactory
operator|.
name|create
argument_list|(
operator|new
name|Project
argument_list|(
name|Project
operator|.
name|nameKey
argument_list|(
name|projectName
argument_list|)
argument_list|)
argument_list|,
name|user
argument_list|,
name|RefOperationValidators
operator|.
name|getCommand
argument_list|(
name|update
argument_list|,
name|operationType
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|refValidators
operator|.
name|validateForRefOperation
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ValidationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

