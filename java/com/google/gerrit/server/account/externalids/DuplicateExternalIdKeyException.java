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
DECL|package|com.google.gerrit.server.account.externalids
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|account
operator|.
name|externalids
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
name|exceptions
operator|.
name|DuplicateKeyException
import|;
end_import

begin_comment
comment|/**  * Exception that is thrown if an external ID cannot be inserted because an external ID with the  * same key already exists.  */
end_comment

begin_class
DECL|class|DuplicateExternalIdKeyException
specifier|public
class|class
name|DuplicateExternalIdKeyException
extends|extends
name|DuplicateKeyException
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|field|duplicateKey
specifier|private
specifier|final
name|ExternalId
operator|.
name|Key
name|duplicateKey
decl_stmt|;
DECL|method|DuplicateExternalIdKeyException (ExternalId.Key duplicateKey)
specifier|public
name|DuplicateExternalIdKeyException
parameter_list|(
name|ExternalId
operator|.
name|Key
name|duplicateKey
parameter_list|)
block|{
name|super
argument_list|(
literal|"Duplicate external ID key: "
operator|+
name|duplicateKey
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|duplicateKey
operator|=
name|duplicateKey
expr_stmt|;
block|}
DECL|method|getDuplicateKey ()
specifier|public
name|ExternalId
operator|.
name|Key
name|getDuplicateKey
parameter_list|()
block|{
return|return
name|duplicateKey
return|;
block|}
block|}
end_class

end_unit

