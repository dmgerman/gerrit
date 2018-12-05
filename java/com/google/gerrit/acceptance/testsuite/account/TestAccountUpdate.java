begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.testsuite.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|testsuite
operator|.
name|account
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|auto
operator|.
name|value
operator|.
name|AutoValue
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
name|acceptance
operator|.
name|testsuite
operator|.
name|ThrowingConsumer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_class
annotation|@
name|AutoValue
DECL|class|TestAccountUpdate
specifier|public
specifier|abstract
class|class
name|TestAccountUpdate
block|{
DECL|method|fullname ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|fullname
parameter_list|()
function_decl|;
DECL|method|httpPassword ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|httpPassword
parameter_list|()
function_decl|;
DECL|method|preferredEmail ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|preferredEmail
parameter_list|()
function_decl|;
DECL|method|username ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|username
parameter_list|()
function_decl|;
DECL|method|status ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|status
parameter_list|()
function_decl|;
DECL|method|active ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|Boolean
argument_list|>
name|active
parameter_list|()
function_decl|;
DECL|method|accountUpdater ()
specifier|abstract
name|ThrowingConsumer
argument_list|<
name|TestAccountUpdate
argument_list|>
name|accountUpdater
parameter_list|()
function_decl|;
DECL|method|builder (ThrowingConsumer<TestAccountUpdate> accountUpdater)
specifier|public
specifier|static
name|Builder
name|builder
parameter_list|(
name|ThrowingConsumer
argument_list|<
name|TestAccountUpdate
argument_list|>
name|accountUpdater
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_TestAccountUpdate
operator|.
name|Builder
argument_list|()
operator|.
name|accountUpdater
argument_list|(
name|accountUpdater
argument_list|)
operator|.
name|httpPassword
argument_list|(
literal|"http-pass"
argument_list|)
return|;
block|}
annotation|@
name|AutoValue
operator|.
name|Builder
DECL|class|Builder
specifier|public
specifier|abstract
specifier|static
class|class
name|Builder
block|{
DECL|method|fullname (String fullname)
specifier|public
specifier|abstract
name|Builder
name|fullname
parameter_list|(
name|String
name|fullname
parameter_list|)
function_decl|;
DECL|method|clearFullname ()
specifier|public
name|Builder
name|clearFullname
parameter_list|()
block|{
return|return
name|fullname
argument_list|(
literal|""
argument_list|)
return|;
block|}
DECL|method|httpPassword (String httpPassword)
specifier|public
specifier|abstract
name|Builder
name|httpPassword
parameter_list|(
name|String
name|httpPassword
parameter_list|)
function_decl|;
DECL|method|clearHttpPassword ()
specifier|public
name|Builder
name|clearHttpPassword
parameter_list|()
block|{
return|return
name|httpPassword
argument_list|(
literal|""
argument_list|)
return|;
block|}
DECL|method|preferredEmail (String preferredEmail)
specifier|public
specifier|abstract
name|Builder
name|preferredEmail
parameter_list|(
name|String
name|preferredEmail
parameter_list|)
function_decl|;
DECL|method|clearPreferredEmail ()
specifier|public
name|Builder
name|clearPreferredEmail
parameter_list|()
block|{
return|return
name|preferredEmail
argument_list|(
literal|""
argument_list|)
return|;
block|}
DECL|method|username (String username)
specifier|public
specifier|abstract
name|Builder
name|username
parameter_list|(
name|String
name|username
parameter_list|)
function_decl|;
DECL|method|clearUsername ()
specifier|public
name|Builder
name|clearUsername
parameter_list|()
block|{
return|return
name|username
argument_list|(
literal|""
argument_list|)
return|;
block|}
DECL|method|status (String status)
specifier|public
specifier|abstract
name|Builder
name|status
parameter_list|(
name|String
name|status
parameter_list|)
function_decl|;
DECL|method|clearStatus ()
specifier|public
name|Builder
name|clearStatus
parameter_list|()
block|{
return|return
name|status
argument_list|(
literal|""
argument_list|)
return|;
block|}
DECL|method|active (boolean active)
specifier|abstract
name|Builder
name|active
parameter_list|(
name|boolean
name|active
parameter_list|)
function_decl|;
DECL|method|active ()
specifier|public
name|Builder
name|active
parameter_list|()
block|{
return|return
name|active
argument_list|(
literal|true
argument_list|)
return|;
block|}
DECL|method|inactive ()
specifier|public
name|Builder
name|inactive
parameter_list|()
block|{
return|return
name|active
argument_list|(
literal|false
argument_list|)
return|;
block|}
DECL|method|accountUpdater (ThrowingConsumer<TestAccountUpdate> accountUpdater)
specifier|abstract
name|Builder
name|accountUpdater
parameter_list|(
name|ThrowingConsumer
argument_list|<
name|TestAccountUpdate
argument_list|>
name|accountUpdater
parameter_list|)
function_decl|;
DECL|method|autoBuild ()
specifier|abstract
name|TestAccountUpdate
name|autoBuild
parameter_list|()
function_decl|;
DECL|method|update ()
specifier|public
name|void
name|update
parameter_list|()
block|{
name|TestAccountUpdate
name|accountUpdate
init|=
name|autoBuild
argument_list|()
decl_stmt|;
name|accountUpdate
operator|.
name|accountUpdater
argument_list|()
operator|.
name|acceptAndThrowSilently
argument_list|(
name|accountUpdate
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

