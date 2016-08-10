begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.mail
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|mail
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|validator
operator|.
name|routines
operator|.
name|DomainValidator
operator|.
name|ArrayType
operator|.
name|GENERIC_PLUS
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|validator
operator|.
name|routines
operator|.
name|DomainValidator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|validator
operator|.
name|routines
operator|.
name|EmailValidator
import|;
end_import

begin_class
DECL|class|OutgoingEmailValidator
specifier|public
class|class
name|OutgoingEmailValidator
block|{
static|static
block|{
name|DomainValidator
operator|.
name|updateTLDOverride
argument_list|(
name|GENERIC_PLUS
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"local"
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|isValid (String addr)
specifier|public
specifier|static
name|boolean
name|isValid
parameter_list|(
name|String
name|addr
parameter_list|)
block|{
return|return
name|EmailValidator
operator|.
name|getInstance
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|)
operator|.
name|isValid
argument_list|(
name|addr
argument_list|)
return|;
block|}
block|}
end_class

end_unit

