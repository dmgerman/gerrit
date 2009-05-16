begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_class
DECL|class|TrustedExternalId
specifier|public
specifier|final
class|class
name|TrustedExternalId
block|{
DECL|class|Key
specifier|public
specifier|static
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
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
annotation|@
name|Column
DECL|field|pattern
specifier|protected
name|String
name|pattern
decl_stmt|;
DECL|method|Key ()
specifier|protected
name|Key
parameter_list|()
block|{     }
DECL|method|Key (final String re)
specifier|public
name|Key
parameter_list|(
specifier|final
name|String
name|re
parameter_list|)
block|{
name|pattern
operator|=
name|re
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|String
name|get
parameter_list|()
block|{
return|return
name|pattern
return|;
block|}
annotation|@
name|Override
DECL|method|set (String newValue)
specifier|protected
name|void
name|set
parameter_list|(
name|String
name|newValue
parameter_list|)
block|{
name|pattern
operator|=
name|newValue
expr_stmt|;
block|}
block|}
DECL|method|isIdentityTrustable ( final Collection<TrustedExternalId> trusted, final Iterable<AccountExternalId> ids)
specifier|public
specifier|static
name|boolean
name|isIdentityTrustable
parameter_list|(
specifier|final
name|Collection
argument_list|<
name|TrustedExternalId
argument_list|>
name|trusted
parameter_list|,
specifier|final
name|Iterable
argument_list|<
name|AccountExternalId
argument_list|>
name|ids
parameter_list|)
block|{
for|for
control|(
specifier|final
name|AccountExternalId
name|e
range|:
name|ids
control|)
block|{
if|if
condition|(
operator|!
name|isTrusted
argument_list|(
name|e
argument_list|,
name|trusted
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
DECL|method|isTrusted (final AccountExternalId id, final Collection<TrustedExternalId> trusted)
specifier|public
specifier|static
name|boolean
name|isTrusted
parameter_list|(
specifier|final
name|AccountExternalId
name|id
parameter_list|,
specifier|final
name|Collection
argument_list|<
name|TrustedExternalId
argument_list|>
name|trusted
parameter_list|)
block|{
if|if
condition|(
name|id
operator|.
name|getExternalId
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"Google Account "
argument_list|)
condition|)
block|{
comment|// Assume this is a trusted token, its a legacy import from
comment|// a fairly well respected provider and only takes effect if
comment|// the administrator has the import still enabled
comment|//
return|return
literal|true
return|;
block|}
if|if
condition|(
name|id
operator|.
name|getExternalId
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"mailto:"
argument_list|)
condition|)
block|{
comment|// mailto identities are created by sending a unique validation
comment|// token to the address and asking them to come back to the site
comment|// with that token.
comment|//
return|return
literal|true
return|;
block|}
for|for
control|(
specifier|final
name|TrustedExternalId
name|t
range|:
name|trusted
control|)
block|{
if|if
condition|(
name|t
operator|.
name|matches
argument_list|(
name|id
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Column
DECL|field|idPattern
specifier|protected
name|Key
name|idPattern
decl_stmt|;
DECL|method|TrustedExternalId ()
specifier|protected
name|TrustedExternalId
parameter_list|()
block|{   }
DECL|method|TrustedExternalId (final TrustedExternalId.Key k)
specifier|public
name|TrustedExternalId
parameter_list|(
specifier|final
name|TrustedExternalId
operator|.
name|Key
name|k
parameter_list|)
block|{
name|idPattern
operator|=
name|k
expr_stmt|;
block|}
DECL|method|getKey ()
specifier|public
name|TrustedExternalId
operator|.
name|Key
name|getKey
parameter_list|()
block|{
return|return
name|idPattern
return|;
block|}
DECL|method|getIdPattern ()
specifier|public
name|String
name|getIdPattern
parameter_list|()
block|{
return|return
name|idPattern
operator|.
name|pattern
return|;
block|}
DECL|method|matches (final AccountExternalId id)
specifier|public
name|boolean
name|matches
parameter_list|(
specifier|final
name|AccountExternalId
name|id
parameter_list|)
block|{
specifier|final
name|String
name|p
init|=
name|getIdPattern
argument_list|()
decl_stmt|;
if|if
condition|(
name|p
operator|.
name|startsWith
argument_list|(
literal|"^"
argument_list|)
operator|&&
name|p
operator|.
name|endsWith
argument_list|(
literal|"$"
argument_list|)
condition|)
block|{
return|return
name|id
operator|.
name|getExternalId
argument_list|()
operator|.
name|matches
argument_list|(
name|p
argument_list|)
return|;
block|}
return|return
name|id
operator|.
name|getExternalId
argument_list|()
operator|.
name|startsWith
argument_list|(
name|p
argument_list|)
return|;
block|}
block|}
end_class

end_unit

