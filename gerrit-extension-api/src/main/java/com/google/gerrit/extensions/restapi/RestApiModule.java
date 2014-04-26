begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.restapi
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|restapi
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
name|annotations
operator|.
name|Export
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
name|annotations
operator|.
name|Exports
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
name|AbstractModule
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
name|Provider
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
name|TypeLiteral
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
name|binder
operator|.
name|LinkedBindingBuilder
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
name|binder
operator|.
name|ScopedBindingBuilder
import|;
end_import

begin_comment
comment|/** Guice DSL for binding {@link RestView} implementations. */
end_comment

begin_class
DECL|class|RestApiModule
specifier|public
specifier|abstract
class|class
name|RestApiModule
extends|extends
name|AbstractModule
block|{
DECL|field|GET
specifier|protected
specifier|static
specifier|final
name|String
name|GET
init|=
literal|"GET"
decl_stmt|;
DECL|field|PUT
specifier|protected
specifier|static
specifier|final
name|String
name|PUT
init|=
literal|"PUT"
decl_stmt|;
DECL|field|DELETE
specifier|protected
specifier|static
specifier|final
name|String
name|DELETE
init|=
literal|"DELETE"
decl_stmt|;
DECL|field|POST
specifier|protected
specifier|static
specifier|final
name|String
name|POST
init|=
literal|"POST"
decl_stmt|;
specifier|protected
parameter_list|<
name|R
extends|extends
name|RestResource
parameter_list|>
DECL|method|get (TypeLiteral<RestView<R>> viewType)
name|ReadViewBinder
argument_list|<
name|R
argument_list|>
name|get
parameter_list|(
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|R
argument_list|>
argument_list|>
name|viewType
parameter_list|)
block|{
return|return
operator|new
name|ReadViewBinder
argument_list|<>
argument_list|(
name|view
argument_list|(
name|viewType
argument_list|,
name|GET
argument_list|,
literal|"/"
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
parameter_list|<
name|R
extends|extends
name|RestResource
parameter_list|>
DECL|method|put (TypeLiteral<RestView<R>> viewType)
name|ModifyViewBinder
argument_list|<
name|R
argument_list|>
name|put
parameter_list|(
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|R
argument_list|>
argument_list|>
name|viewType
parameter_list|)
block|{
return|return
operator|new
name|ModifyViewBinder
argument_list|<>
argument_list|(
name|view
argument_list|(
name|viewType
argument_list|,
name|PUT
argument_list|,
literal|"/"
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
parameter_list|<
name|R
extends|extends
name|RestResource
parameter_list|>
DECL|method|post (TypeLiteral<RestView<R>> viewType)
name|ModifyViewBinder
argument_list|<
name|R
argument_list|>
name|post
parameter_list|(
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|R
argument_list|>
argument_list|>
name|viewType
parameter_list|)
block|{
return|return
operator|new
name|ModifyViewBinder
argument_list|<>
argument_list|(
name|view
argument_list|(
name|viewType
argument_list|,
name|POST
argument_list|,
literal|"/"
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
parameter_list|<
name|R
extends|extends
name|RestResource
parameter_list|>
DECL|method|delete (TypeLiteral<RestView<R>> viewType)
name|ModifyViewBinder
argument_list|<
name|R
argument_list|>
name|delete
parameter_list|(
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|R
argument_list|>
argument_list|>
name|viewType
parameter_list|)
block|{
return|return
operator|new
name|ModifyViewBinder
argument_list|<>
argument_list|(
name|view
argument_list|(
name|viewType
argument_list|,
name|DELETE
argument_list|,
literal|"/"
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
parameter_list|<
name|R
extends|extends
name|RestResource
parameter_list|>
DECL|method|get (TypeLiteral<RestView<R>> viewType, String name)
name|ReadViewBinder
argument_list|<
name|R
argument_list|>
name|get
parameter_list|(
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|R
argument_list|>
argument_list|>
name|viewType
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|ReadViewBinder
argument_list|<>
argument_list|(
name|view
argument_list|(
name|viewType
argument_list|,
name|GET
argument_list|,
name|name
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
parameter_list|<
name|R
extends|extends
name|RestResource
parameter_list|>
DECL|method|put (TypeLiteral<RestView<R>> viewType, String name)
name|ModifyViewBinder
argument_list|<
name|R
argument_list|>
name|put
parameter_list|(
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|R
argument_list|>
argument_list|>
name|viewType
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|ModifyViewBinder
argument_list|<>
argument_list|(
name|view
argument_list|(
name|viewType
argument_list|,
name|PUT
argument_list|,
name|name
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
parameter_list|<
name|R
extends|extends
name|RestResource
parameter_list|>
DECL|method|post (TypeLiteral<RestView<R>> viewType, String name)
name|ModifyViewBinder
argument_list|<
name|R
argument_list|>
name|post
parameter_list|(
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|R
argument_list|>
argument_list|>
name|viewType
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|ModifyViewBinder
argument_list|<>
argument_list|(
name|view
argument_list|(
name|viewType
argument_list|,
name|POST
argument_list|,
name|name
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
parameter_list|<
name|R
extends|extends
name|RestResource
parameter_list|>
DECL|method|delete (TypeLiteral<RestView<R>> viewType, String name)
name|ModifyViewBinder
argument_list|<
name|R
argument_list|>
name|delete
parameter_list|(
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|R
argument_list|>
argument_list|>
name|viewType
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|ModifyViewBinder
argument_list|<>
argument_list|(
name|view
argument_list|(
name|viewType
argument_list|,
name|DELETE
argument_list|,
name|name
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
parameter_list|<
name|P
extends|extends
name|RestResource
parameter_list|>
DECL|method|child (TypeLiteral<RestView<P>> type, String name)
name|ChildCollectionBinder
argument_list|<
name|P
argument_list|>
name|child
parameter_list|(
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|P
argument_list|>
argument_list|>
name|type
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|ChildCollectionBinder
argument_list|<>
argument_list|(
name|view
argument_list|(
name|type
argument_list|,
name|GET
argument_list|,
name|name
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
parameter_list|<
name|R
extends|extends
name|RestResource
parameter_list|>
DECL|method|view ( TypeLiteral<RestView<R>> viewType, String method, String name)
name|LinkedBindingBuilder
argument_list|<
name|RestView
argument_list|<
name|R
argument_list|>
argument_list|>
name|view
parameter_list|(
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|R
argument_list|>
argument_list|>
name|viewType
parameter_list|,
name|String
name|method
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
name|bind
argument_list|(
name|viewType
argument_list|)
operator|.
name|annotatedWith
argument_list|(
name|export
argument_list|(
name|method
argument_list|,
name|name
argument_list|)
argument_list|)
return|;
block|}
DECL|method|export (String method, String name)
specifier|private
specifier|static
name|Export
name|export
parameter_list|(
name|String
name|method
parameter_list|,
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|.
name|length
argument_list|()
operator|>
literal|1
operator|&&
name|name
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
comment|// Views may be bound as "/" to mean the resource itself, or
comment|// as "status" as in "/type/{id}/status". Don't bind "/status"
comment|// if the caller asked for that, bind what the server expects.
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|Exports
operator|.
name|named
argument_list|(
name|method
operator|+
literal|"."
operator|+
name|name
argument_list|)
return|;
block|}
DECL|class|ReadViewBinder
specifier|public
specifier|static
class|class
name|ReadViewBinder
parameter_list|<
name|P
extends|extends
name|RestResource
parameter_list|>
block|{
DECL|field|binder
specifier|private
specifier|final
name|LinkedBindingBuilder
argument_list|<
name|RestView
argument_list|<
name|P
argument_list|>
argument_list|>
name|binder
decl_stmt|;
DECL|method|ReadViewBinder (LinkedBindingBuilder<RestView<P>> binder)
specifier|private
name|ReadViewBinder
parameter_list|(
name|LinkedBindingBuilder
argument_list|<
name|RestView
argument_list|<
name|P
argument_list|>
argument_list|>
name|binder
parameter_list|)
block|{
name|this
operator|.
name|binder
operator|=
name|binder
expr_stmt|;
block|}
specifier|public
parameter_list|<
name|T
extends|extends
name|RestReadView
argument_list|<
name|P
argument_list|>
parameter_list|>
DECL|method|to (Class<T> impl)
name|ScopedBindingBuilder
name|to
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|impl
parameter_list|)
block|{
return|return
name|binder
operator|.
name|to
argument_list|(
name|impl
argument_list|)
return|;
block|}
specifier|public
parameter_list|<
name|T
extends|extends
name|RestReadView
argument_list|<
name|P
argument_list|>
parameter_list|>
DECL|method|toInstance (T impl)
name|void
name|toInstance
parameter_list|(
name|T
name|impl
parameter_list|)
block|{
name|binder
operator|.
name|toInstance
argument_list|(
name|impl
argument_list|)
expr_stmt|;
block|}
specifier|public
parameter_list|<
name|T
extends|extends
name|RestReadView
argument_list|<
name|P
argument_list|>
parameter_list|>
DECL|method|toProvider (Class<? extends Provider<? extends T>> providerType)
name|ScopedBindingBuilder
name|toProvider
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Provider
argument_list|<
name|?
extends|extends
name|T
argument_list|>
argument_list|>
name|providerType
parameter_list|)
block|{
return|return
name|binder
operator|.
name|toProvider
argument_list|(
name|providerType
argument_list|)
return|;
block|}
specifier|public
parameter_list|<
name|T
extends|extends
name|RestReadView
argument_list|<
name|P
argument_list|>
parameter_list|>
DECL|method|toProvider (Provider<? extends T> provider)
name|ScopedBindingBuilder
name|toProvider
parameter_list|(
name|Provider
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|provider
parameter_list|)
block|{
return|return
name|binder
operator|.
name|toProvider
argument_list|(
name|provider
argument_list|)
return|;
block|}
block|}
DECL|class|ModifyViewBinder
specifier|public
specifier|static
class|class
name|ModifyViewBinder
parameter_list|<
name|P
extends|extends
name|RestResource
parameter_list|>
block|{
DECL|field|binder
specifier|private
specifier|final
name|LinkedBindingBuilder
argument_list|<
name|RestView
argument_list|<
name|P
argument_list|>
argument_list|>
name|binder
decl_stmt|;
DECL|method|ModifyViewBinder (LinkedBindingBuilder<RestView<P>> binder)
specifier|private
name|ModifyViewBinder
parameter_list|(
name|LinkedBindingBuilder
argument_list|<
name|RestView
argument_list|<
name|P
argument_list|>
argument_list|>
name|binder
parameter_list|)
block|{
name|this
operator|.
name|binder
operator|=
name|binder
expr_stmt|;
block|}
specifier|public
parameter_list|<
name|T
extends|extends
name|RestModifyView
argument_list|<
name|P
argument_list|,
name|?
argument_list|>
parameter_list|>
DECL|method|to (Class<T> impl)
name|ScopedBindingBuilder
name|to
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|impl
parameter_list|)
block|{
return|return
name|binder
operator|.
name|to
argument_list|(
name|impl
argument_list|)
return|;
block|}
specifier|public
parameter_list|<
name|T
extends|extends
name|RestModifyView
argument_list|<
name|P
argument_list|,
name|?
argument_list|>
parameter_list|>
DECL|method|toInstance (T impl)
name|void
name|toInstance
parameter_list|(
name|T
name|impl
parameter_list|)
block|{
name|binder
operator|.
name|toInstance
argument_list|(
name|impl
argument_list|)
expr_stmt|;
block|}
specifier|public
parameter_list|<
name|T
extends|extends
name|RestModifyView
argument_list|<
name|P
argument_list|,
name|?
argument_list|>
parameter_list|>
DECL|method|toProvider (Class<? extends Provider<? extends T>> providerType)
name|ScopedBindingBuilder
name|toProvider
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Provider
argument_list|<
name|?
extends|extends
name|T
argument_list|>
argument_list|>
name|providerType
parameter_list|)
block|{
return|return
name|binder
operator|.
name|toProvider
argument_list|(
name|providerType
argument_list|)
return|;
block|}
specifier|public
parameter_list|<
name|T
extends|extends
name|RestModifyView
argument_list|<
name|P
argument_list|,
name|?
argument_list|>
parameter_list|>
DECL|method|toProvider (Provider<? extends T> provider)
name|ScopedBindingBuilder
name|toProvider
parameter_list|(
name|Provider
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|provider
parameter_list|)
block|{
return|return
name|binder
operator|.
name|toProvider
argument_list|(
name|provider
argument_list|)
return|;
block|}
block|}
DECL|class|ChildCollectionBinder
specifier|public
specifier|static
class|class
name|ChildCollectionBinder
parameter_list|<
name|P
extends|extends
name|RestResource
parameter_list|>
block|{
DECL|field|binder
specifier|private
specifier|final
name|LinkedBindingBuilder
argument_list|<
name|RestView
argument_list|<
name|P
argument_list|>
argument_list|>
name|binder
decl_stmt|;
DECL|method|ChildCollectionBinder (LinkedBindingBuilder<RestView<P>> binder)
specifier|private
name|ChildCollectionBinder
parameter_list|(
name|LinkedBindingBuilder
argument_list|<
name|RestView
argument_list|<
name|P
argument_list|>
argument_list|>
name|binder
parameter_list|)
block|{
name|this
operator|.
name|binder
operator|=
name|binder
expr_stmt|;
block|}
specifier|public
parameter_list|<
name|C
extends|extends
name|RestResource
parameter_list|,
name|T
extends|extends
name|ChildCollection
argument_list|<
name|P
argument_list|,
name|C
argument_list|>
parameter_list|>
DECL|method|to (Class<T> impl)
name|ScopedBindingBuilder
name|to
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|impl
parameter_list|)
block|{
return|return
name|binder
operator|.
name|to
argument_list|(
name|impl
argument_list|)
return|;
block|}
specifier|public
parameter_list|<
name|C
extends|extends
name|RestResource
parameter_list|,
name|T
extends|extends
name|ChildCollection
argument_list|<
name|P
argument_list|,
name|C
argument_list|>
parameter_list|>
DECL|method|toInstance (T impl)
name|void
name|toInstance
parameter_list|(
name|T
name|impl
parameter_list|)
block|{
name|binder
operator|.
name|toInstance
argument_list|(
name|impl
argument_list|)
expr_stmt|;
block|}
specifier|public
parameter_list|<
name|C
extends|extends
name|RestResource
parameter_list|,
name|T
extends|extends
name|ChildCollection
argument_list|<
name|P
argument_list|,
name|C
argument_list|>
parameter_list|>
DECL|method|toProvider (Class<? extends Provider<? extends T>> providerType)
name|ScopedBindingBuilder
name|toProvider
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Provider
argument_list|<
name|?
extends|extends
name|T
argument_list|>
argument_list|>
name|providerType
parameter_list|)
block|{
return|return
name|binder
operator|.
name|toProvider
argument_list|(
name|providerType
argument_list|)
return|;
block|}
specifier|public
parameter_list|<
name|C
extends|extends
name|RestResource
parameter_list|,
name|T
extends|extends
name|ChildCollection
argument_list|<
name|P
argument_list|,
name|C
argument_list|>
parameter_list|>
DECL|method|toProvider (Provider<? extends T> provider)
name|ScopedBindingBuilder
name|toProvider
parameter_list|(
name|Provider
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|provider
parameter_list|)
block|{
return|return
name|binder
operator|.
name|toProvider
argument_list|(
name|provider
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

