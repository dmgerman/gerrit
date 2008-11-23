begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
DECL|package|com.google.gerrit.client.reviewdb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|reviewdb
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|Column
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
name|client
operator|.
name|StringKey
import|;
end_import

begin_comment
comment|/** Global configuration needed to serve web requests. */
end_comment

begin_class
DECL|class|SystemConfig
specifier|public
specifier|final
class|class
name|SystemConfig
block|{
DECL|class|Key
specifier|public
specifier|static
specifier|final
class|class
name|Key
extends|extends
name|StringKey
argument_list|<
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|Key
argument_list|<
name|?
argument_list|>
argument_list|>
block|{
DECL|field|VALUE
specifier|private
specifier|static
specifier|final
name|String
name|VALUE
init|=
literal|"X"
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|length
operator|=
literal|1
argument_list|)
DECL|field|one
specifier|protected
name|String
name|one
init|=
name|VALUE
decl_stmt|;
DECL|method|Key ()
specifier|public
name|Key
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|String
name|get
parameter_list|()
block|{
return|return
name|VALUE
return|;
block|}
block|}
DECL|method|create ()
specifier|public
specifier|static
name|SystemConfig
name|create
parameter_list|()
block|{
specifier|final
name|SystemConfig
name|r
init|=
operator|new
name|SystemConfig
argument_list|()
decl_stmt|;
name|r
operator|.
name|singleton
operator|=
operator|new
name|SystemConfig
operator|.
name|Key
argument_list|()
expr_stmt|;
name|r
operator|.
name|maxSessionAge
operator|=
literal|12
operator|*
literal|60
operator|*
literal|60
comment|/* seconds */
expr_stmt|;
return|return
name|r
return|;
block|}
annotation|@
name|Column
DECL|field|singleton
specifier|protected
name|Key
name|singleton
decl_stmt|;
comment|/** Private key to sign XSRF protection tokens. */
annotation|@
name|Column
argument_list|(
name|length
operator|=
literal|36
argument_list|)
DECL|field|xsrfPrivateKey
specifier|public
name|String
name|xsrfPrivateKey
decl_stmt|;
comment|/** Private key to sign account identification cookies. */
annotation|@
name|Column
argument_list|(
name|length
operator|=
literal|36
argument_list|)
DECL|field|accountPrivateKey
specifier|public
name|String
name|accountPrivateKey
decl_stmt|;
comment|/** Maximum web session age, in seconds. */
annotation|@
name|Column
DECL|field|maxSessionAge
specifier|public
name|int
name|maxSessionAge
decl_stmt|;
DECL|method|SystemConfig ()
specifier|protected
name|SystemConfig
parameter_list|()
block|{   }
block|}
end_class

end_unit

