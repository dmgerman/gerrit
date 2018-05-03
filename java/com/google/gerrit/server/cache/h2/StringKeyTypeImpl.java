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
DECL|package|com.google.gerrit.server.cache.h2
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|cache
operator|.
name|h2
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|hash
operator|.
name|Funnel
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|hash
operator|.
name|Funnels
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|PreparedStatement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|ResultSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
import|;
end_import

begin_enum
DECL|enum|StringKeyTypeImpl
enum|enum
name|StringKeyTypeImpl
implements|implements
name|KeyType
argument_list|<
name|String
argument_list|>
block|{
DECL|enumConstant|INSTANCE
name|INSTANCE
block|;
annotation|@
name|Override
DECL|method|columnType ()
specifier|public
name|String
name|columnType
parameter_list|()
block|{
return|return
literal|"VARCHAR(4096)"
return|;
block|}
annotation|@
name|Override
DECL|method|get (ResultSet rs, int col)
specifier|public
name|String
name|get
parameter_list|(
name|ResultSet
name|rs
parameter_list|,
name|int
name|col
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|rs
operator|.
name|getString
argument_list|(
name|col
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|set (PreparedStatement ps, int col, String value)
specifier|public
name|void
name|set
parameter_list|(
name|PreparedStatement
name|ps
parameter_list|,
name|int
name|col
parameter_list|,
name|String
name|value
parameter_list|)
throws|throws
name|SQLException
block|{
name|ps
operator|.
name|setString
argument_list|(
name|col
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
DECL|method|funnel ()
specifier|public
name|Funnel
argument_list|<
name|String
argument_list|>
name|funnel
parameter_list|()
block|{
name|Funnel
argument_list|<
name|?
argument_list|>
name|s
init|=
name|Funnels
operator|.
name|unencodedCharsFunnel
argument_list|()
decl_stmt|;
return|return
operator|(
name|Funnel
argument_list|<
name|String
argument_list|>
operator|)
name|s
return|;
block|}
block|}
end_enum

end_unit

